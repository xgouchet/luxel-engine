package art.luxels.imageio

import art.luxels.imageio.color.SDRColor
import art.luxels.imageio.color.asSDR
import art.luxels.imageio.test.stub.StubRasterData
import com.goncalossilva.resources.Resource
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import okio.Buffer
import okio.Path.Companion.toPath

class ImageIOSpec : DescribeSpec(
    {

        val baseName = "test_raster"
        val stubRaster = StubRasterData(32, 32)
        val tempOutputDirPath = "test_output".toPath()

        describe("writing data") {

            it("write to a file") {
                checkAll(Arb.element(ImageIO.writeableFormats)) { format ->
                    val outputFileName = "$baseName.${format.fileNameExtension}"
                    val outputFile = tempOutputDirPath / outputFileName

                    ImageIO.write(stubRaster, format.constraints, tempOutputDirPath, baseName)

                    val outputMetadata = fileSystem.metadataOrNull(outputFile)
                    outputMetadata?.isRegularFile shouldBe true
                }
            }

            it("write to a sink") {

                checkAll(Arb.element(ImageIO.writeableFormats)) { format ->
                    val buffer = Buffer()
                    val refFileName = "reference.${format.fileNameExtension}"
                    val refResourcePath = "src/commonTest/resources/references/$refFileName"
                    val refResource = Resource(refResourcePath)
                    val refData = refResource.readBytes()

                    ImageIO.write(stubRaster, format.constraints, buffer)

                    val writtenData = buffer.readByteArray()
                    writtenData shouldBe refData
                }
            }
        }

        describe("reading data") {

            ImageIO.writeableFormats.forEach { format ->
                if (format in ImageIO.readableFormats) {
                    it("reads from a ${format.fileNameExtension} file") {
                        val outputFileName = "$baseName.${format.fileNameExtension}"
                        val outputFile = tempOutputDirPath / outputFileName

                        ImageIO.write(stubRaster, format.constraints, tempOutputDirPath, baseName)
                        val readRaster = ImageIO.read(outputFile)
                        ImageIO.write(readRaster, format.constraints, tempOutputDirPath, "test_read")

                        readRaster.width shouldBe stubRaster.width
                        readRaster.height shouldBe stubRaster.height

                        for (i in 0..<readRaster.width) {
                            for (j in 0..<readRaster.height) {
                                withClue("Unexpected pixel color at [$i, $j]") {
                                    val readColor = readRaster.getColor(i, j).asSDR()
                                    val expectedColor = stubRaster.getColor(i, j).asSDR()

                                    if (format.constraints.isTransparencySupported) {
                                        readColor shouldBe expectedColor
                                    } else {
                                        readColor shouldBe expectedColor.copy(a = SDRColor.MAX_VALUE)
                                    }
                                }
                            }
                        }
                    }

                    it("reads from a ${format.fileNameExtension} source") {
                        val buffer = Buffer()

                        ImageIO.write(stubRaster, format.constraints, buffer)
                        val readRaster = ImageIO.read(format, buffer)

                        readRaster.width shouldBe stubRaster.width
                        readRaster.height shouldBe stubRaster.height
                        for (i in 0..<readRaster.width) {
                            for (j in 0..<readRaster.height) {
                                val readColor = readRaster.getColor(i, j).asSDR()
                                val expectedColor = stubRaster.getColor(i, j).asSDR()

                                if (format.constraints.isTransparencySupported) {
                                    readColor shouldBe expectedColor
                                } else {
                                    readColor shouldBe expectedColor.copy(a = SDRColor.MAX_VALUE)
                                }
                            }
                        }
                    }
                }
            }
        }
    },
)

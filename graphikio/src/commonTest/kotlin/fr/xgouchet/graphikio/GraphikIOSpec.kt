package fr.xgouchet.graphikio

import com.goncalossilva.resources.Resource
import fr.xgouchet.graphikio.color.asSDR
import fr.xgouchet.graphikio.test.stub.StubRasterData
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import okio.Buffer
import okio.Path.Companion.toPath

class GraphikIOSpec : DescribeSpec({

    val baseName = "test_raster"
    val stubRaster = StubRasterData(32, 32)
    val tempOutputDirPath = "test_output".toPath()

    describe("writing data") {

        it("write to a file") {
            checkAll(Arb.element(GraphikIO.writeableFormats)) { format ->
                val outputFileName = "$baseName.${format.fileNameExtension}"
                val outputFile = tempOutputDirPath / outputFileName

                GraphikIO.write(stubRaster, format.constraints, tempOutputDirPath, baseName)

                val outputMetadata = fileSystem.metadataOrNull(outputFile)
                outputMetadata?.isRegularFile shouldBe true
            }
        }

        it("write to a sink") {

            checkAll(Arb.element(GraphikIO.writeableFormats)) { format ->
                val buffer = Buffer()
                val refFileName = "reference.${format.fileNameExtension}"
                val refResourcePath = "src/commonTest/resources/references/$refFileName"
                val refResource = Resource(refResourcePath)
                val refData = refResource.readBytes()

                GraphikIO.write(stubRaster, format.constraints, buffer)

                val writtenData = buffer.readByteArray()
                writtenData shouldBe refData
            }
        }
    }

    describe("reading data") {

        it("reads from a file") {
            checkAll(Arb.element(GraphikIO.writeableFormats)) { format ->
                if (format in GraphikIO.readableFormats) {
                    val outputFileName = "$baseName.${format.fileNameExtension}"
                    val outputFile = tempOutputDirPath / outputFileName

                    GraphikIO.write(stubRaster, format.constraints, tempOutputDirPath, baseName)
                    val readRaster = GraphikIO.read(outputFile)
                    GraphikIO.write(readRaster, format.constraints, tempOutputDirPath, "test_read")

                    readRaster.width shouldBe stubRaster.width
                    readRaster.height shouldBe stubRaster.height
                    // TODO adapt assertion to type of format (SDR vs HDR, Alpha, …)
                    for (i in 0..<readRaster.width) {
                        for (j in 0..<readRaster.height) {
                            withClue("Unexpected pixel color at [$i, $j]") {
                                readRaster.getColor(i, j).asSDR() shouldBe stubRaster.getColor(i, j).asSDR()
                            }
                        }
                    }
                }
            }
        }

        it("reads from a source") {
            checkAll(Arb.element(GraphikIO.writeableFormats)) { format ->
                if (format in GraphikIO.readableFormats) {
                    val buffer = Buffer()

                    GraphikIO.write(stubRaster, format.constraints, buffer)
                    val readRaster = GraphikIO.read(format, buffer)

                    readRaster.width shouldBe stubRaster.width
                    readRaster.height shouldBe stubRaster.height
                    for (i in 0..<readRaster.width) {
                        for (j in 0..<readRaster.height) {
                            readRaster.getColor(i, j).asSDR() shouldBe stubRaster.getColor(i, j).asSDR()
                        }
                    }
                }
            }
        }
    }
})

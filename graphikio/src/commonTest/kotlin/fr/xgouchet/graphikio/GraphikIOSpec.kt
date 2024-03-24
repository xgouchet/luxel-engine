package fr.xgouchet.graphikio

import fr.xgouchet.graphikio.test.stub.StubRasterData
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import okio.Buffer
import okio.Path.Companion.toPath

class GraphikIOSpec : DescribeSpec({

    describe("writing to the file system") {

        it("Writes the expected files") {
            val stubRaster = StubRasterData(12, 12)
            checkAll(Arb.element(GraphikIO.supportedFormats)) { format ->
                GraphikIO.write(
                    stubRaster,
                    format,
                    "test_output".toPath(),
                    "test_raster",
                )
            }
        }

        it("Writes the expected data") {
            val stubRaster = StubRasterData(32, 32)
            checkAll(Arb.element(GraphikIO.supportedFormats)) { format ->
                val buffer = Buffer()
                GraphikIO.write(
                    stubRaster,
                    format,
                    buffer,
                )

                println("${format.fileNameExtension} buffer has size ${buffer.size}")
            }
        }
    }
})

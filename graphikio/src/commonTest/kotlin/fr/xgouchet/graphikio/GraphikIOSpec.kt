package fr.xgouchet.graphikio

import fr.xgouchet.graphikio.test.stub.StubRasterData
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import okio.Path.Companion.toPath

class GraphikIOSpec : DescribeSpec({

    describe("writing to the file system") {
        val stubRaster = StubRasterData(512, 512)

        checkAll(Arb.element(GraphikIO.supportedFormats)) { format ->
            GraphikIO.write(
                stubRaster,
                format,
                "test_output".toPath(),
                "test_raster",
            )
        }
    }
})

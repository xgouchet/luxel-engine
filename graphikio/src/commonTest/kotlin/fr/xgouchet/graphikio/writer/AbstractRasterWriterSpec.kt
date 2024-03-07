package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.test.kotest.property.imageSizeIntArb
import fr.xgouchet.graphikio.test.stub.StubRasterData
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import okio.Buffer

@Suppress("TestFunctionName")
fun <W : RasterWriter> AbstractRasterWriterSpec(
    writerProvider: () -> W,
) = describeSpec {
    describe("generic writer") {
        it("writes to a sink") {
            checkAll(imageSizeIntArb(), imageSizeIntArb()) { w, h ->
                val writer = writerProvider()
                val input = StubRasterData(w, h)
                val sink = Buffer()

                writer.write(input, sink)

                sink.size shouldBeGreaterThan 0
            }
        }

        it("reads all the pixels") {
            checkAll(imageSizeIntArb(), imageSizeIntArb()) { w, h ->
                val writer = writerProvider()
                val input = StubRasterData(w, h)
                val sink = Buffer()

                writer.write(input, sink)

                sink.size shouldBeGreaterThan 0
            }
        }

        it("throws if the raster is empty and doesn't write") {
            val writer = writerProvider()
            val input = StubRasterData(0, 0)
            val sink = Buffer()

            shouldThrow<IllegalStateException> {
                writer.write(input, sink)
            }
            sink.size shouldBe 0
        }
    }
}

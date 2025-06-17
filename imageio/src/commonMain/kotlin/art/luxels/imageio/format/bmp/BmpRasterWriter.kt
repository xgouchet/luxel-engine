package art.luxels.imageio.format.bmp

import art.luxels.imageio.api.AbstractRasterWriter
import art.luxels.imageio.api.RasterWriter
import art.luxels.imageio.color.asSDR
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.format.bmp.BmpImageFormat.BITS_PER_PIXEL
import art.luxels.imageio.format.bmp.BmpImageFormat.BMP_HEADER_SIZE
import art.luxels.imageio.format.bmp.BmpImageFormat.BMP_PREFIX
import art.luxels.imageio.format.bmp.BmpImageFormat.BYTES_PER_PIXEL
import art.luxels.imageio.format.bmp.BmpImageFormat.BYTE_ALIGN_COUNT
import art.luxels.imageio.format.bmp.BmpImageFormat.DIB_HEADER_SIZE
import art.luxels.imageio.format.bmp.BmpImageFormat.PIXEL_DATA_OFFSET
import okio.BufferedSink
import okio.Sink
import okio.buffer

/**
 * A [RasterWriter] supporting the [BmpImageFormat].
 */
class BmpRasterWriter : AbstractRasterWriter(BmpImageFormat) {
    // region RasterWriter

    override fun write(rasterData: RasterData, sink: Sink) {
        val bytesPerRow = BYTES_PER_PIXEL * rasterData.width
        val modulo = bytesPerRow.mod(BYTE_ALIGN_COUNT)
        val bytesPerPadding = if (modulo == 0) 0 else (BYTE_ALIGN_COUNT - modulo)
        val bytesPerRowPadded = bytesPerRow + bytesPerPadding

        val pixelDataSize = bytesPerRowPadded * rasterData.height
        val fileSize = BMP_HEADER_SIZE + DIB_HEADER_SIZE + pixelDataSize

        check(pixelDataSize > 0) { "Writer can't write an empty raster" }

        val bufferedSink = sink.buffer()

        // BMP Header
        writeBmpHeader(bufferedSink, fileSize)

        // DIB Header
        val header = DibHeader.BitmapInfoHeader(
            rasterData.width,
            rasterData.height,
            BITS_PER_PIXEL,
        )
        header.write(bufferedSink)

        // Pixel data
        rasterData.prepare()
        for (y in rasterData.height - 1 downTo 0) {
            for (x in 0..<rasterData.width) {
                val color = rasterData.getColor(x, y).asSDR()
                bufferedSink.writeByte(color.b)
                bufferedSink.writeByte(color.g)
                bufferedSink.writeByte(color.r)
            }

            // Padding for 4-byte alignment
            for (p in 0..<bytesPerPadding) {
                bufferedSink.writeByte(0)
            }
        }

        bufferedSink.flush()
        bufferedSink.close()
    }

    // endregion

    // region Internal

    private fun writeBmpHeader(bufferedSink: BufferedSink, fileSize: Int) {
        bufferedSink.writeUtf8(BMP_PREFIX)
        bufferedSink.writeIntLe(fileSize)
        bufferedSink.writeShortLe(0) // Unused.
        bufferedSink.writeShortLe(0) // Unused.
        bufferedSink.writeIntLe(PIXEL_DATA_OFFSET)
    }

    //  endregion
}

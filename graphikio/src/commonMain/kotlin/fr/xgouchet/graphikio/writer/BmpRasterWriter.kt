package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.color.asBoundColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.BmpImageFormat
import okio.BufferedSink
import okio.Sink
import okio.buffer

class BmpRasterWriter : AbstractRasterWriter(BmpImageFormat) {

    override fun write(rasterData: RasterData, sink: Sink) {
        val bytesPerRow = BYTES_PER_PIXEL * rasterData.width
        val modulo = bytesPerRow.mod(4)
        // Compute a full row byte count ensuring a 4 bytes alignment
        val bytesPerPadding = if (modulo == 0) 0 else (4 - modulo)
        val bytesPerRowPadded = bytesPerRow + bytesPerPadding

        val pixelDataSize = bytesPerRowPadded * rasterData.height
        val fileSize = BMP_HEADER_SIZE + DIB_HEADER_SIZE + pixelDataSize

        check(pixelDataSize > 0) { "Writer can't write an empty raster" }

        val bufferedSink = sink.buffer()

        // BMP Header
        writeBmpHeader(bufferedSink, fileSize)

        // DIB Header
        writeDibHeader(bufferedSink, rasterData)

        // Pixel data.
        for (y in rasterData.height - 1 downTo 0) {
            for (x in 0..<rasterData.width) {
                val color = rasterData.getColor(x, y).asBoundColor()
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
    }

    private fun writeBmpHeader(bufferedSink: BufferedSink, fileSize: Int) {
        bufferedSink.writeUtf8(BMP_PREFIX)
        bufferedSink.writeIntLe(fileSize)
        bufferedSink.writeShortLe(0) // Unused.
        bufferedSink.writeShortLe(0) // Unused.
        bufferedSink.writeIntLe(PIXEL_DATA_OFFSET)
    }

    private fun writeDibHeader(bufferedSink: BufferedSink, rasterData: RasterData) {
        bufferedSink.writeIntLe(DIB_HEADER_SIZE)
        bufferedSink.writeIntLe(rasterData.width)
        bufferedSink.writeIntLe(rasterData.height)
        bufferedSink.writeShortLe(COLOR_PLANE_COUNT)
        bufferedSink.writeShortLe(BITS_PER_PIXEL)
        bufferedSink.writeIntLe(COMPRESSION)
        bufferedSink.writeIntLe(16) // Size of bitmap data including padding.
        bufferedSink.writeIntLe(PRINT_RESOLUTION_PPM) // Horizontal
        bufferedSink.writeIntLe(PRINT_RESOLUTION_PPM) // Vertical
        bufferedSink.writeIntLe(COLOR_PALETTE_SIZE)
        bufferedSink.writeIntLe(IMPORTANT_COLOR_COUNT)
    }

    companion object {
        private const val BMP_PREFIX = "BM"
        private const val BYTES_PER_PIXEL = 3
        private const val BITS_PER_PIXEL = BYTES_PER_PIXEL * Byte.SIZE_BITS

        private const val BMP_HEADER_SIZE = 14
        private const val DIB_HEADER_SIZE = 40
        private const val PIXEL_DATA_OFFSET = BMP_HEADER_SIZE + DIB_HEADER_SIZE
        private const val COLOR_PLANE_COUNT = 1
        private const val COLOR_PALETTE_SIZE = 0
        private const val IMPORTANT_COLOR_COUNT = 0
        private const val COMPRESSION = 0

        /** Print resolution in Pixel per Meter. 72 ppi ~ 2835 ppm */
        private const val PRINT_RESOLUTION_PPM = 2835
    }
}

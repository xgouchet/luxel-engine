package fr.xgouchet.graphikio.format.bmp

import fr.xgouchet.graphikio.api.AbstractRasterReader
import fr.xgouchet.graphikio.api.RasterReader
import fr.xgouchet.graphikio.color.SDRColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.data.SDRRasterData
import fr.xgouchet.graphikio.format.bmp.BmpImageFormat.BMP_HEADER_SIZE
import fr.xgouchet.graphikio.format.bmp.BmpImageFormat.BYTES_PER_PIXEL
import okio.BufferedSource
import okio.Source
import okio.buffer

/**
 * A [RasterReader] to read standard Bitmap files.
 */
class BmpRasterReader : AbstractRasterReader(BmpImageFormat) {

    // region RasterReader

    override fun read(source: Source): RasterData {
        val bufferedSource = source.buffer()

        val (_, pixelDataOffset) = readBmpHeader(bufferedSource)
        val dibHeader = DibHeader.readDibHeader(bufferedSource)
        check(pixelDataOffset == BMP_HEADER_SIZE + dibHeader.headerSize) {
            "Invalid pixel data offset $pixelDataOffset, expected ${BMP_HEADER_SIZE + dibHeader.headerSize}"
        }
        check(dibHeader.bitPerPixel == 24)

        val bytesPerRow = BYTES_PER_PIXEL * dibHeader.width
        val modulo = bytesPerRow.mod(BmpImageFormat.BYTE_ALIGN_COUNT)
        val bytesPerPadding = if (modulo == 0) 0 else (BmpImageFormat.BYTE_ALIGN_COUNT - modulo)
//        val bytesPerRowPadded = bytesPerRow + bytesPerPadding

        val rasterData = SDRRasterData(dibHeader.width, dibHeader.height)

        for (y in rasterData.height - 1 downTo 0) {
            for (x in 0..<dibHeader.width) {
                val b = bufferedSource.readByte()
                val g = bufferedSource.readByte()
                val r = bufferedSource.readByte()

                rasterData.setColor(x, y, SDRColor(r, g, b))
            }

            // Padding for 4-byte alignment
            for (p in 0..<bytesPerPadding) {
                bufferedSource.readByte()
            }
        }

        return rasterData
    }

    // endregion

    // region Internal

    private fun readBmpHeader(source: BufferedSource): Pair<Int, Int> {
        val prefix = source.readUtf8(2)
        check(prefix == BmpImageFormat.BMP_PREFIX)

        val fileSize = source.readIntLe()
        source.readShortLe() // Unused
        source.readShortLe() // Unused
        val pixelDataOffset = source.readIntLe()

        return fileSize to pixelDataOffset
    }

    // endregion
}

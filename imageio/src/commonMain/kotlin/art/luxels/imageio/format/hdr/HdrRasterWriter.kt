package art.luxels.imageio.format.hdr

import art.luxels.imageio.api.AbstractRasterWriter
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.color.asHDR
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.data.pixelCount
import okio.BufferedSink
import okio.Sink
import okio.buffer
import kotlin.math.max

/**
 * Implementation based on Paul Bourke's format page: https://paulbourke.net/dataformats/pic/
 * and Víctor Arellano's Java implementation https://github.com/Ivelate/JavaHDR
 */
class HdrRasterWriter : AbstractRasterWriter(HdrImageFormat) {
    // region RasterWriter

    override fun write(rasterData: RasterData, sink: Sink) {
        check(rasterData.pixelCount > 0) { "Writer can't write an empty raster" }

        val buffer = sink.buffer()
        writeHDR(rasterData, buffer)
        buffer.flush()
        buffer.close()
    }

    // endregion

    // region Internal

    private fun writeHDR(rasterData: RasterData, out: BufferedSink) {
        val width = rasterData.width
        val height = rasterData.height

        val rgbeData = prepareRgbeData(rasterData)

        writeHDR(rgbeData, width, height, out)
    }

    private fun prepareRgbeData(rasterData: RasterData): ByteArray {
        rasterData.prepare()
        val width = rasterData.width
        val height = rasterData.height
        val rgbeData = ByteArray(width * height * (4))
        for (j in 0..<height) {
            for (i in 0..<width) {
                val color = rasterData.getColor(i, j).asHDR()
                val s = j * width * 3
                val offset = i + ((s / 3) * 4)
                float2rgbe(rgbeData, color, offset, width)
            }
        }
        return rgbeData
    }

    private fun writeHeader(
        out: BufferedSink,
        width: Int,
        height: Int,
    ) {
        out.writeUtf8(HEADER_ID)
        out.writeUtf8(HEADER_SEP)
        out.writeUtf8(HEADER_FORMAT)
        out.writeUtf8(HEADER_SEP)
        out.writeUtf8(HEADER_SEP)
        out.writeUtf8("-Y $height +X $width")
        out.writeUtf8(HEADER_SEP)
    }

    private fun writeDataRgb(out: BufferedSink, rgbeData: ByteArray) {
        val buff = ByteArray(1024)
        var bindex = 0
        val pixelCount = rgbeData.size / 4

        for (i in 0..<pixelCount) {
            buff[bindex] = rgbeData[i]
            buff[bindex + 1] = rgbeData[pixelCount + i]
            buff[bindex + 2] = rgbeData[2 * pixelCount + i]
            buff[bindex + 3] = rgbeData[3 * pixelCount + i]
            bindex += 4
            if (bindex + 4 >= buff.size || i == pixelCount - 1) {
                out.write(buff, 0, bindex)
                bindex = 0
            }
        }
    }

    // TODO rewrite the RLE algorithm
    @Suppress("NestedBlockDepth", "CognitiveComplexMethod")
    private fun writeDataRLE(
        out: BufferedSink,
        rgbeData: ByteArray,
        scanlineWidth: Int,
    ) {
        val scanlineHeader = byteArrayOf(
            2,
            2,
            ((scanlineWidth shr 8) and 0xFF).toByte(),
            (scanlineWidth and 0xFF).toByte(),
        ) // Scanline header with the scanline width in args

        val scanlineBuf = ByteArray(scanlineWidth * 2)
        var scanlineBufIndex = 0
        var curr = 0
        var rgbeCont = 0 // 0 - R iteration , 1 - G iteration , 2 - B iteration , 3 - E iteration

        // For each scanline (R and E of the same scanline count as different scanlines)
        var scanlineEnd = scanlineWidth
        while (scanlineEnd <= rgbeData.size) {
            var auxIndex = curr
            var isRepeating = rgbeData[curr + 1] == rgbeData[curr]

            while (curr < scanlineEnd - 1) {
                curr++
                val isEqual = rgbeData[curr] == rgbeData[auxIndex]
                val fromAux = curr - auxIndex
                if (isEqual != isRepeating || fromAux >= 127) {
                    if (isRepeating) {
                        scanlineBuf[scanlineBufIndex++] = (128 + fromAux).toByte()
                        scanlineBuf[scanlineBufIndex++] = rgbeData[auxIndex]
                    } else {
                        scanlineBuf[scanlineBufIndex++] = fromAux.toByte()
                        for (i in 0..<fromAux) {
                            scanlineBuf[scanlineBufIndex++] = rgbeData[auxIndex + i]
                        }
                    }
                    auxIndex = curr
                    isRepeating = curr != scanlineEnd - 1 && rgbeData[curr + 1] == rgbeData[curr]
                }
            }
            curr++
            val fromAux = curr - auxIndex
            if (isRepeating) {
                scanlineBuf[scanlineBufIndex++] = (128 + fromAux).toByte()
                scanlineBuf[scanlineBufIndex++] = rgbeData[auxIndex]
            } else {
                scanlineBuf[scanlineBufIndex++] = fromAux.toByte()
                for (i in 0..<fromAux) {
                    scanlineBuf[scanlineBufIndex++] = rgbeData[auxIndex + i]
                }
            }

            if (rgbeCont == 0) out.write(scanlineHeader)

            out.write(scanlineBuf, 0, scanlineBufIndex)

            rgbeCont += (1)
            if (rgbeCont > 3) rgbeCont = 0
            scanlineBufIndex = 0
            scanlineEnd += scanlineWidth
        }
    }

    private fun writeHDR(
        rgbeData: ByteArray,
        width: Int,
        height: Int,
        out: BufferedSink,
    ) {
        writeHeader(out, width, height)
        if (width in 8..32767) {
            writeDataRLE(out, rgbeData, width)
        } else {
            writeDataRgb(out, rgbeData)
        }
    }

    private fun float2rgbe(
        rgbeData: ByteArray,
        color: HDRColor,
        off: Int,
        separation: Int,
    ) {
        var v = max(color.r, (max(color.g, color.b)))

        if (v < 1.0E-32f) {
            rgbeData[off + separation * 3] = 0
            rgbeData[off + separation * 2] = rgbeData[off + separation * 3]
            rgbeData[off + separation] = rgbeData[off + separation * 2]
            rgbeData[off] = rgbeData[off + separation]
        } else {
            val fe = HdrDouble.fromDouble(v)
            v = (fe.fraction * 256.0 / v)
            rgbeData[off] = (color.r * v).toInt().toByte()
            rgbeData[off + separation] = (color.g * v).toInt().toByte()
            rgbeData[off + separation * 2] = (color.b * v).toInt().toByte()
            rgbeData[off + separation * 3] = (fe.exponent + 128).toByte()
        }
    }

    // endregion

    companion object {
        private const val HEADER_SEP = "\n"
        private const val HEADER_ID = "#?RGBE"
        private const val HEADER_FORMAT = "FORMAT=32-bit_rle_rgbe"
    }
}

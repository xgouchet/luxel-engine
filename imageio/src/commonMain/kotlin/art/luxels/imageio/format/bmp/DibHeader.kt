package art.luxels.imageio.format.bmp

import art.luxels.imageio.io.Writeable
import okio.BufferedSink
import okio.BufferedSource

internal sealed class DibHeader(
    val width: Int,
    val height: Int,
    val bitPerPixel: Int,
    val colorPlaneCount: Int = DEFAULT_COLOR_PLANE_COUNT,
) : Writeable {

    init {
        check(width > 0)
        check(height > 0)
        check(bitPerPixel > 0)
        val bitPerChannel = bitPerPixel / 3
        check((bitPerChannel != 0) && ((bitPerChannel and (bitPerChannel - 1)) == 0))
    }

    abstract val headerSize: Int

    class BitmapCoreHeader(
        width: Int,
        height: Int,
        colorPlaneCount: Int,
        bitPerPixel: Int,
    ) : DibHeader(width, height, colorPlaneCount, bitPerPixel) {

        // region DibHeader

        override val headerSize: Int = DIB_HEADER_SIZE

        // endregion

        // region Writeable

        override fun write(sink: BufferedSink) {
            sink.writeIntLe(DIB_HEADER_SIZE)
            // Unlike all other DIB headers width and height are stored as unsigned short
            sink.writeShortLe(width)
            sink.writeShortLe(height)
            sink.writeShortLe(colorPlaneCount)
            sink.writeShortLe(bitPerPixel)
        }

        // endregion

        companion object {

            internal val DIB_HEADER_SIZE: Int = 12

            fun read(size: Int, source: BufferedSource): BitmapCoreHeader {
                check(size >= DIB_HEADER_SIZE)
                // Unlike all other DIB headers width and height are stored as unsigned short
                val width = source.readShortLe().toInt()
                val height = source.readShortLe().toInt()

                val colorPlaneCount = source.readShortLe().toInt()
                check(colorPlaneCount == 1) {
                    "Bitmap file Core header uses unsupported multiple color planes ($colorPlaneCount)"
                }

                val bitPerPixel = source.readShortLe().toInt()

                return BitmapCoreHeader(width, height, bitPerPixel, colorPlaneCount)
            }
        }
    }

    @Suppress("LongParameterList")
    class BitmapInfoHeader(
        width: Int,
        height: Int,
        bitPerPixel: Int,
        colorPlaneCount: Int = DEFAULT_COLOR_PLANE_COUNT,
        val horizontalResolutionPPM: Int = DEFAULT_RESOLUTION_PPM,
        val verticalResolutionPPM: Int = DEFAULT_RESOLUTION_PPM,
        val colorPaletteSize: Int = DEFAULT_COLOR_PALETTE_SIZE,
        val importantColorCount: Int = DEFAULT_IMPORTANT_COLOR_COUNT,
    ) : DibHeader(width, height, bitPerPixel, colorPlaneCount) {

        // region DibHeader

        override val headerSize: Int = DIB_HEADER_SIZE

        // endregion

        // region Writeable

        override fun write(sink: BufferedSink) {
            sink.writeIntLe(DIB_HEADER_SIZE)
            sink.writeIntLe(width)
            sink.writeIntLe(height)
            sink.writeShortLe(colorPlaneCount)
            sink.writeShortLe(bitPerPixel)

            sink.writeIntLe(BmpImageFormat.COMPRESSION_BI_RGB)
            sink.writeIntLe(0) // Size of bitmap data including padding, only when data is compressed

            sink.writeIntLe(horizontalResolutionPPM)
            sink.writeIntLe(verticalResolutionPPM)
            sink.writeIntLe(colorPaletteSize)
            sink.writeIntLe(importantColorCount)
        }

        // endregion

        companion object {

            internal const val DIB_HEADER_SIZE: Int = 40

            internal const val DEFAULT_COLOR_PALETTE_SIZE = 0
            internal const val DEFAULT_RESOLUTION_PPM = 2835
            internal const val DEFAULT_IMPORTANT_COLOR_COUNT = 0

            fun read(size: Int, source: BufferedSource): BitmapInfoHeader {
                check(size >= BitmapCoreHeader.DIB_HEADER_SIZE)
                // Unlike all other DIB headers width and height are stored as unsigned short
                val width = source.readIntLe()
                val height = source.readIntLe()

                val colorPlaneCount = source.readShortLe().toInt()
                check(colorPlaneCount == 1) {
                    "Bitmap file Info header uses unsupported multiple color planes ($colorPlaneCount)"
                }
                val bitPerPixel = source.readShortLe().toInt()

                val compression = source.readIntLe()
                check(compression == BmpImageFormat.COMPRESSION_BI_RGB) {
                    "Bitmap file Info header uses an unsupported compression flag: $compression"
                }

                source.readIntLe() // Image Size, unused

                val xPPM = source.readIntLe()
                val yPPM = source.readIntLe()

                val paletteSize = source.readIntLe()
                val importantColorCount = source.readIntLe()

                return BitmapInfoHeader(
                    width = width,
                    height = height,
                    bitPerPixel = bitPerPixel,
                    colorPlaneCount = colorPlaneCount,
                    horizontalResolutionPPM = xPPM,
                    verticalResolutionPPM = yPPM,
                    colorPaletteSize = paletteSize,
                    importantColorCount = importantColorCount,
                )
            }
        }
    }

    @Suppress("LongParameterList")
    class BitmapInfoHeaderV5(
        width: Int,
        height: Int,
        bitPerPixel: Int,
        colorPlaneCount: Int = DEFAULT_COLOR_PLANE_COUNT,
        val horizontalResolutionPPM: Int = DEFAULT_RESOLUTION_PPM,
        val verticalResolutionPPM: Int = DEFAULT_RESOLUTION_PPM,
        val colorPaletteSize: Int = DEFAULT_COLOR_PALETTE_SIZE,
        val importantColorCount: Int = DEFAULT_IMPORTANT_COLOR_COUNT,
    ) : DibHeader(width, height, bitPerPixel, colorPlaneCount) {

        // region DibHeader

        override val headerSize: Int = DIB_HEADER_SIZE

        // endregion

        // region Writeable

        override fun write(sink: BufferedSink) {
            sink.writeIntLe(DIB_HEADER_SIZE)
            sink.writeIntLe(width)
            sink.writeIntLe(height)
            sink.writeShortLe(colorPlaneCount)
            sink.writeShortLe(bitPerPixel)

            sink.writeIntLe(BmpImageFormat.COMPRESSION_BI_RGB)
            sink.writeIntLe(0) // Size of bitmap data including padding, only when data is compressed

            sink.writeIntLe(horizontalResolutionPPM)
            sink.writeIntLe(verticalResolutionPPM)
            sink.writeIntLe(colorPaletteSize)
            sink.writeIntLe(importantColorCount)
        }

        // endregion

        companion object {

            internal const val DIB_HEADER_SIZE: Int = 124

            internal const val DEFAULT_COLOR_PALETTE_SIZE = 0
            internal const val DEFAULT_RESOLUTION_PPM = 2835
            internal const val DEFAULT_IMPORTANT_COLOR_COUNT = 0

            fun read(size: Int, source: BufferedSource): BitmapInfoHeaderV5 {
                check(size >= art.luxels.imageio.format.bmp.DibHeader.BitmapCoreHeader.DIB_HEADER_SIZE)
                // Unlike all other DIB headers width and height are stored as unsigned short
                val width = source.readIntLe()
                val height = source.readIntLe()

                val colorPlaneCount = source.readShortLe().toInt()
                check(colorPlaneCount == 1) {
                    "Bitmap file Header v5 uses unsupported multiple color planes ($colorPlaneCount)"
                }
                val bitPerPixel = source.readShortLe().toInt()

                val compression = source.readIntLe()
                check(compression == BmpImageFormat.COMPRESSION_BI_RGB) {
                    "Bitmap file Header v5 uses an unsupported compression flag: $compression"
                }

                source.readIntLe() // Image Size, unused

                val xPPM = source.readIntLe()
                val yPPM = source.readIntLe()

                val paletteSize = source.readIntLe()
                val importantColorCount = source.readIntLe()

                val masks = IntArray(4)

                for (i in masks.indices) {
                    masks[i] = source.readIntLe()
                }

                val colorSpaceType = source.readIntLe()
                val cieXYZEndpoints = DoubleArray(9)

                for (i in cieXYZEndpoints.indices) {
                    cieXYZEndpoints[i] = source.readIntLe().toDouble() // TODO: Hmmm...?
                }

                val gamma = IntArray(3)

                for (i in gamma.indices) {
                    gamma[i] = source.readIntLe()
                }

                val intent = source.readIntLe() // TODO: Verify if this is same as ICC intent
                val profileData = source.readIntLe().toLong() and 0xffffffffL
                val profileSize = source.readIntLe().toLong() and 0xffffffffL
                val reserved = source.readIntLe() // Reserved

                print(
                    buildString {
                        append("Some attributes are not fully used in this reader : ")
                        append("colorSpaceType:$colorSpaceType,")
                        append("cieXYZEndpoints:[${cieXYZEndpoints.joinToString()}],")
                        append("gamma:[${gamma.joinToString()}],")
                        append("intent:$intent,")
                        append("profileData:$profileData,")
                        append("profileSize:$profileSize,")
                        append("reserved:$reserved,")
                    },
                )

                return BitmapInfoHeaderV5(
                    width = width,
                    height = height,
                    bitPerPixel = bitPerPixel,
                    colorPlaneCount = colorPlaneCount,
                    horizontalResolutionPPM = xPPM,
                    verticalResolutionPPM = yPPM,
                    colorPaletteSize = paletteSize,
                    importantColorCount = importantColorCount,
                )
            }
        }
    }

    companion object {

        internal const val DEFAULT_COLOR_PLANE_COUNT = 1

        fun readDibHeader(source: BufferedSource): DibHeader {
            val headerSize = source.readIntLe()

            return when (headerSize) {
                BitmapCoreHeader.DIB_HEADER_SIZE -> BitmapCoreHeader.read(headerSize, source)
                BitmapInfoHeader.DIB_HEADER_SIZE -> BitmapInfoHeader.read(headerSize, source)
                BitmapInfoHeaderV5.DIB_HEADER_SIZE -> BitmapInfoHeaderV5.read(headerSize, source)
                else -> throw IllegalArgumentException("Unsupported header size $headerSize")
            }
        }
    }
}

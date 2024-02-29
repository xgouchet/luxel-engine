package fr.xgouchet.luxels.core.io

import com.github.ivelate.JavaHDR.HDREncoder
import com.github.ivelate.JavaHDR.HDRImageRGB
import fr.xgouchet.luxels.core.render.exposure.Film
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset

/**
 * Image Fixer implementation writing a film data to a Radiance HDR file.
 *
 * Implementation based on Paul Bourke's format page: https://paulbourke.net/dataformats/pic/
 * and other sources online
 */
class HdrImageFixer(
    iso: Double = BASE_ISO,
    // TODO Greyscale version ?
) : ImageFixer {

    private val isoFactor = iso / BASE_ISO

    // region ImageFixer

    override fun write(film: Film, outputName: String) {
        val outputFile = File(File("output"), "$outputName.$OUTPUT_FORMAT")
        val image = HDRImageRGB(film.width, film.height)

        for (i in 0..<film.width) {
            for (j in 0..<film.height) {
                val color = film.getColor(i, j) * isoFactor
                image.setPixelValue(i, j, 0, color.r.toFloat())
                image.setPixelValue(i, j, 1, color.g.toFloat())
                image.setPixelValue(i, j, 2, color.b.toFloat())
            }
        }

        HDREncoder.writeHDR(image, outputFile)
    }

    // endregion

    // region Internal

    private fun writeCustom(film: Film, outputFile: File) {
        val outputStream = outputFile.outputStream()

        try {
            outputStream.use { writeToStream(film, it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeToStream(film: Film, outputStream: OutputStream) {
        val width = film.width
        val height = film.height

        writeHeader(film, outputStream)

//        for (i in 0..<width) {
//            for (j in 0..<height) {
//                val color = film.getColor(i, j) * isoFactor
//                image.setPixelValue(i, j, 0, color.r.toFloat())
//                image.setPixelValue(i, j, 1, color.g.toFloat())
//                image.setPixelValue(i, j, 2, color.b.toFloat())
//            }
//        }
    }

    private fun writeHeader(film: Film, outputStream: OutputStream) {
        outputStream.write(HEADER_PREFIX)
        outputStream.write(HEADER_FORMAT)
        outputStream.write(HEADER_SOFTWARE)
        outputStream.write(HEADER_EMPTY_LINE)
        outputStream.write("-Y ${film.height} +X ${film.width}\n")
        outputStream.write(HEADER_EMPTY_LINE)
    }

    private fun OutputStream.write(text: String, charset: Charset = Charsets.UTF_8) {
        write(text.toByteArray(charset))
    }

    // endregion

    companion object {

        private val HEADER_PREFIX = "#?RADIANCE\n"
        private val HEADER_FORMAT = "FORMAT=32-bit_rle_rgbe\n"
        private val HEADER_SOFTWARE = "SOFTWARE=Luxel 1.0\n" // TODO inject version number :)
        private val HEADER_EMPTY_LINE = "\n"

        const val OUTPUT_FORMAT = "hdr"

        internal const val BASE_ISO = 1000.0
    }
}

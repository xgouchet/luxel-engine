package art.luxels.components.io

import art.luxels.core.io.ImageFixer
import art.luxels.core.render.Film
import art.luxels.imageio.ImageIO
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.format.ImageFormat
import okio.Path

/**
 * An abstract [ImageFixer] based on the imageio module capabilities.
 * @param targetFormat the [ImageFormat] to use when writing an image
 * @param outputDirPath the path to the output dir where the file should be saved
 */
abstract class AbstractRasterImageFixer(
    private val targetFormat: ImageFormat,
    private val outputDirPath: Path,
) : ImageFixer {
    // region ImageFixer

    override fun write(film: Film, outputName: String) {
        val rasterData = convertFilmToRasterData(film)

        ImageIO.write(
            rasterData,
            targetFormat.constraints,
            outputDirPath,
            outputName,
        )
    }

    //  endregion

    // region AbstractRasterImageWrapper

    /**
     * Converts an internal [Film] into a writeable [RasterData] instance.
     * @param film the film to convert
     * @return a [RasterData] object to be written
     */
    abstract fun convertFilmToRasterData(film: Film): RasterData

    // endregion
}

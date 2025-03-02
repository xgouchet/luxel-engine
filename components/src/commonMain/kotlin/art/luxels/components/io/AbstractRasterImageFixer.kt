package art.luxels.components.io

import art.luxels.core.io.ImageFixer
import art.luxels.core.render.Film
import art.luxels.imageio.ImageIO
import art.luxels.imageio.format.ImageFormat
import okio.Path

/**
 * An abstract [ImageFixer] based on the imageio module capabilities.
 * @param targetFormat the [ImageFormat] to use when writing an image
 * @param outputDirPath the path to the output dir where the file should be saved
 * @param iso the sensitivity of the film (can be useful when exporting to SDR formats)
 */
abstract class AbstractRasterImageFixer(
    private val targetFormat: ImageFormat,
    private val outputDirPath: Path,
    private val iso: Double = 1.0,
) : ImageFixer {
    // region ImageFixer

    override fun write(film: Film, outputName: String) {
        val rasterData = FilmRasterDataWrapper(film, iso)

        ImageIO.write(
            rasterData,
            targetFormat.constraints,
            outputDirPath,
            outputName,
        )
    }

    //  endregion
}

package art.luxels.components.io

import art.luxels.components.io.converter.FilmConverter
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
 * @param filmConverter the [FilmConverter] to transform the film into serializable [RasterData]
 */
class RasterImageFixer(
    private val targetFormat: ImageFormat,
    private val outputDirPath: Path,
    private val filmConverter: FilmConverter,
) : ImageFixer {

    // region ImageFixer

    override fun write(film: Film, outputName: String) {
        val rasterData = filmConverter.convert(film)

        ImageIO.write(
            rasterData,
            targetFormat.constraints,
            outputDirPath,
            outputName,
        )
    }

    //  endregion
}

package art.luxels.components.io

import art.luxels.core.io.ImageFixer
import art.luxels.core.render.Film
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.format.bmp.BmpImageFormat
import okio.Path

/**
 * An [ImageFixer] exporting an exposed film to a bitmap BMP image file.
 * @param outputDirPath the path to the output dir where the file should be saved
 */
class BmpImageFixer(outputDirPath: Path) :
    AbstractRasterImageFixer(
        BmpImageFormat,
        outputDirPath,
    ) {
    // region AbstractRasterImageFixer

    override fun convertFilmToRasterData(film: Film): RasterData {
        return LevelFilmRasterDataWrapper(film)
    }

    // endregion
}

package fr.xgouchet.luxels.core.io

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.luxels.core.render.exposure.Film
import okio.Path

abstract class AbstractRasterImageFixer(
    val targetFormat: ImageFormat,
    val outputDirPath: Path,
    val iso: Double = 1.0,
) : ImageFixer {

    override fun write(film: Film, outputName: String) {
        val rasterData = FilmRasterDataWrapper(film, iso)

        GraphikIO.write(
            rasterData,
            targetFormat.constraints,
            outputDirPath,
            outputName,
        )
    }
}

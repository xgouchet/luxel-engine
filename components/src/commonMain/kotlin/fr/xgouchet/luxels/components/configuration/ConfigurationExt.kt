package fr.xgouchet.luxels.components.configuration

import fr.xgouchet.luxels.components.io.BmpImageFixer
import fr.xgouchet.luxels.components.io.HdrImageFixer
import fr.xgouchet.luxels.core.configuration.RenderConfigBuilder
import okio.Path
import okio.Path.Companion.toPath

/**
 * Uses an HDR image fixer.
 * @param path the output directory path (default is "./output")
 */
fun RenderConfigBuilder.hdrFixer(path: Path = "output".toPath()) {
    fixer(HdrImageFixer(path))
}

/**
 * Uses a BMP image fixer.
 * @param iso the iso sensitivity of the fixer (the lower, the darker the image, the higher the brighter, default is 400)
 * @param path the output directory path (default is "./output")
 */
fun RenderConfigBuilder.bmpFixer(iso: Double = 400.0, path: Path = "output".toPath()) {
    fixer(BmpImageFixer(path, iso))
}

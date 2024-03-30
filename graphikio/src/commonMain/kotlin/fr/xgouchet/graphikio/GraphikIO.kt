package fr.xgouchet.graphikio

import fr.xgouchet.graphikio.api.RasterWriter
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.ImageFormat
import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.graphikio.format.bmp.BmpRasterWriter
import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import fr.xgouchet.graphikio.format.hdr.HdrRasterWriter
import okio.Path
import okio.Sink

object GraphikIO {

    private val writers: List<RasterWriter> by lazy {
        // TODO automatic lazy loading
        listOf(
            BmpRasterWriter(),
            HdrRasterWriter(),
        )
    }

    val supportedFormats = listOf(
        BmpImageFormat,
        HdrImageFormat,
    )

    fun write(
        rasterData: RasterData,
        imageFormat: ImageFormat,
        parentPath: Path,
        baseName: String,
    ) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormat(imageFormat) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        fileSystem.createDirectories(parentPath)
        val filePath = parentPath.div("$baseName.${imageFormat.fileNameExtension}")
        val sink = fileSystem.sink(filePath, mustCreate = false)

        writer.write(rasterData, sink)
    }

    fun write(
        rasterData: RasterData,
        imageFormat: ImageFormat,
        sink: Sink,
    ) {
        val writer = writers.firstOrNull { writer -> writer.supportsFormat(imageFormat) }
        if (writer == null) {
            throw IllegalArgumentException("No writer found to write the provided image")
        }

        writer.write(rasterData, sink)
    }
}

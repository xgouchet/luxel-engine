package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.format.ImageFormat
import okio.Sink

interface RasterWriter {

    fun supportsFormat(imageFormat: ImageFormat): Boolean

    fun write(rasterData: RasterData, sink: Sink)
}

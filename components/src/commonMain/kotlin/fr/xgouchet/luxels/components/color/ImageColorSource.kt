package fr.xgouchet.luxels.components.color

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.geometry.Vector2
import okio.Path
import kotlin.math.roundToInt

class ImageColorSource(
    private val rasterData: RasterData,
    var uv: Vector2,
) : ColorSource {

    constructor(
        imagePath: Path,
        uv: Vector2 = Vector2.NULL,
    ) : this(GraphikIO.read(imagePath), uv)

    // region ColorSource

    override fun color(): Color {
        val x = (uv.x * rasterData.width).roundToInt()
        val y = (uv.y * rasterData.height).roundToInt()
        return if (x in 0..<rasterData.width && y in 0..<rasterData.height) {
            val hdrColor = rasterData.getColor(x, y).asHDR()
            return Color(hdrColor.r, hdrColor.g, hdrColor.b, hdrColor.a)
        } else {
            Color.TRANSPARENT
        }
    }

    // endregion
}
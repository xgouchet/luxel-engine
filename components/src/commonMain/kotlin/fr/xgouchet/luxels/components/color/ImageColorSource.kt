package fr.xgouchet.luxels.components.color

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.core.color.ColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import okio.Path
import kotlin.math.roundToInt

/**
 * A [ColorSource] based on a image data.
 * @param rasterData the data to read color from
 * @param uv the position on the image to read color from (in a [0…1;0…1] range)
 */
class ImageColorSource(
    private val rasterData: RasterData,
    var uv: Vector<Dimension.D2>,
) : ColorSource {

    /**
     * @param imagePath the path to the image to read
     * @param uv the position on the image to read color from (in a [0…1;0…1] range, default is [0;0])
     */
    constructor(
        imagePath: Path,
        uv: Vector<Dimension.D2> = Vector.nul(Dimension.D2),
    ) : this(GraphikIO.read(imagePath), uv)

    // region ColorSource

    override fun color(): HDRColor {
        val x = (uv.x * rasterData.width).roundToInt()
        val y = (uv.y * rasterData.height).roundToInt()
        return if (x in 0..<rasterData.width && y in 0..<rasterData.height) {
            return rasterData.getColor(x, y).asHDR()
        } else {
            HDRColor.TRANSPARENT
        }
    }

    // endregion
}

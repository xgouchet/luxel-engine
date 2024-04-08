package fr.xgouchet.graphikio.data

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.SDRColor
import fr.xgouchet.graphikio.color.asSDR

/**
 * A [RasterData] implementation storing pixels as an array of [SDRColor].
 */
class SDRRasterData(
    override val width: Int,
    override val height: Int,
) : MutableRasterData {

    private val pixels = Array(pixelCount) { SDRColor.TRANSPARENT }

    // region RasterData

    override fun getColor(x: Int, y: Int): Color {
        return pixels[index(x, y)]
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return emptyMap()
    }

    // endregion

    // region MutableRasterData

    override fun setColor(x: Int, y: Int, c: Color) {
        pixels[index(x, y)] = c.asSDR()
    }

    // endregion

    private fun index(x: Int, y: Int): Int {
        return x + (y * width)
    }
}

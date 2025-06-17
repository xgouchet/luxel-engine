package art.luxels.imageio.data

import art.luxels.imageio.color.Color
import art.luxels.imageio.color.SDRColor
import art.luxels.imageio.color.asSDR

/** * A [RasterData] implementation storing pixels as an array of [SDRColor].
 * @property width the width àf the raster data
 * @property height the height àf the raster data
 * @property outOfBoundStrategy the strategy to use when accessing pixels out of bounds
 */
class SDRRasterData(
    override val width: Int,
    override val height: Int,
    var outOfBoundStrategy: OutOfBoundStrategy = OutOfBoundStrategy.FAIL,
) : MutableRasterData {

    private val pixels = Array(pixelCount) { SDRColor.TRANSPARENT }

    // region RasterData

    override fun prepare() {}

    override fun getColor(x: Int, y: Int): Color {
        val index = index(getXIndex(x), getYIndex(y))
        return pixels[index]
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return emptyMap()
    }

    // endregion

    // region MutableRasterData

    override fun setColor(
        x: Int,
        y: Int,
        c: Color,
    ) {
        val index = index(getXIndex(x), getYIndex(y))
        pixels[index] = c.asSDR()
    }

    // endregion

    // region Internal

    private fun getXIndex(x: Int): Int {
        return if (x < 0 || x >= width) {
            when (outOfBoundStrategy) {
                OutOfBoundStrategy.FAIL -> error("Image x index out of bounds (asked for $x, but width is $width)")

                OutOfBoundStrategy.CLAMP -> width

                OutOfBoundStrategy.LOOP -> if (x < -width) {
                    error("Image x index out of bounds (asked for $x, but width is $width)")
                } else if (x < 0) {
                    x + width
                } else {
                    x % width
                }
            }
        } else {
            x
        }
    }

    private fun getYIndex(y: Int): Int {
        return if (y < 0 || y >= height) {
            when (outOfBoundStrategy) {
                OutOfBoundStrategy.FAIL -> error("Image y index out of bounds (asked for $y, but height is $height)")

                OutOfBoundStrategy.CLAMP -> width

                OutOfBoundStrategy.LOOP -> if (y < -height) {
                    error("Image x index out of bounds (asked for $y, but height is $height)")
                } else if (y < 0) {
                    y + height
                } else {
                    y % height
                }
            }
        } else {
            y
        }
    }

    private fun index(x: Int, y: Int): Int {
        return x + (y * width)
    }

    // endregion
}

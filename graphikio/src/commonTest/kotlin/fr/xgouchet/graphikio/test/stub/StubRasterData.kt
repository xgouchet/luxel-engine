package fr.xgouchet.graphikio.test.stub

import fr.xgouchet.graphikio.color.BoundColor
import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.data.RasterData

/**
 * A picture with only [BoundColor] pixels.
 *  - pixel on the borders are pure black
 *  - pixel in the top-left corner make a gradient of red/green
 *  - pixel in the top-right corner make a gradient of green/blue
 *  - pixel in the bottom-left corner make a gradient of blue/red
 *  - pixel in the bottom-right corner make a gradient of grey/transparency
 */
class StubRasterData(
    override val width: Int,
    override val height: Int,
) : RasterData {

    private val halfWidth = width / 2
    private val halfHeight = height / 2

    private val pixelCount = width * height

    private val pixelRead = IntArray(pixelCount) { 0 }

    override fun getColor(x: Int, y: Int): Color {
        require(x in 0..<width)
        require(y in 0..<height)

        pixelRead[x + (y * width)]++

        if (x == 0 || y == 0) return BoundColor.BLACK
        if (x == (width - 1) || y == (height - 1)) return BoundColor.WHITE

        val kx = ((512 * x) / width).mod(256)
        val ky = ((512 * y) / height).mod(256)

        return if (x < halfWidth) {
            if (y < halfHeight) {
                BoundColor(kx, ky, 0)
            } else {
                BoundColor(ky, 0, kx)
            }
        } else {
            if (y < halfHeight) {
                BoundColor(0, kx, ky)
            } else {
                BoundColor(kx, kx, kx, ky)
            }
        }
    }
}

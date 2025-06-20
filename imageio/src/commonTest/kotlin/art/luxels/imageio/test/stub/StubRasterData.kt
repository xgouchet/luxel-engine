package art.luxels.imageio.test.stub

import art.luxels.imageio.color.Color
import art.luxels.imageio.color.SDRColor
import art.luxels.imageio.data.MetadataAttribute
import art.luxels.imageio.data.RasterData

/**
 * A picture with only [SDRColor] pixels.
 *  - pixel on the borders are pure black
 *  - pixel in the top-left corner make a gradient of red/green
 *  - pixel in the top-right corner make a gradient of green/blue
 *  - pixel in the bottom-left corner make a gradient of blue/red
 *  - pixel in the bottom-right corner make a gradient of grey/transparency
 */
class StubRasterData(override val width: Int, override val height: Int) : RasterData {
    private val halfWidth = width / 2
    private val halfHeight = height / 2

    private val pixelCount = width * height

    private val pixelRead = IntArray(pixelCount) { 0 }

    override fun prepare() {
        // No-op
    }

    override fun getColor(x: Int, y: Int): Color {
        require(x in 0..<width)
        require(y in 0..<height)

        pixelRead[x + (y * width)]++

        if (x == 0 || y == 0) return SDRColor.BLACK
        if (x == (width - 1) || y == (height - 1)) return SDRColor.WHITE

        val kx = ((512 * x) / width).mod(256)
        val ky = ((512 * y) / height).mod(256)

        return if (x < halfWidth) {
            if (y < halfHeight) {
                SDRColor(kx, ky, 0)
            } else {
                SDRColor(ky, 0, kx)
            }
        } else {
            if (y < halfHeight) {
                SDRColor(0, kx, ky)
            } else {
                SDRColor(kx, kx, kx, ky)
            }
        }
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return emptyMap()
    }
}

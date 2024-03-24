package fr.xgouchet.luxels.core.color

import fr.xgouchet.luxels.core.math.Vector2
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

/**
 * An image color source.
 * @param imageFile the file to read from
 * @property uv the UV coordinate (in 0..1 range)
 */
class ImageColorSource(
    private val imageFile: File,
    var uv: Vector2,
) : ColorSource {

    // TODO write tests

    private val imageData: BufferedImage by lazy { ImageIO.read(imageFile) }

    // region ColorSource

    override fun color(): Color {
        val x = (uv.x * imageData.width).roundToInt()
        val y = (uv.y * imageData.height).roundToInt()
        return if (x in 0..<imageData.width && y in 0..<imageData.height) {
            val colorInt = imageData.getRGB(x, y)
            java.awt.Color(colorInt).asColor()
        } else {
            Color.BLACK
        }
    }

    // endregion
}

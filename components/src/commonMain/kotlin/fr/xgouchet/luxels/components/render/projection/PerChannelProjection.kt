package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.engine.render.Projection

/**
 * Allows using distinct projection for each color channel.
 *
 * @param D the dimension of the source position
 *
 * @property redProjection the projection for the red channel
 * @property greenProjection the projection for the green channel
 * @property blueProjection the projection for the blue channel
 */
class PerChannelProjection<D : Dimension>(
    val redProjection: Projection<D>,
    val greenProjection: Projection<D>,
    val blueProjection: Projection<D>,
) : Projection<D> {

    // region Projection

    override fun project(position: Vector<D>, color: Color): List<Pair<Vector<Dimension.D2>, Color>> {
        val red = mapChannel(redProjection, position, color, HDRColor.RED)
        val green = mapChannel(greenProjection, position, color, HDRColor.GREEN)
        val blue = mapChannel(blueProjection, position, color, HDRColor.BLUE)

        return red + green + blue
    }

    // endregion

    // region Internal

    private fun mapChannel(
        projection: Projection<D>,
        position: Vector<D>,
        color: Color,
        channelColor: HDRColor,
    ): List<Pair<Vector<Dimension.D2>, Color>> {
        return projection.project(position, color).map { (p, c) -> p to (c.asHDR() * channelColor) }
    }

    // endregion
}

package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.Vector2
import kotlin.math.roundToInt

/**
 * A basic [Film] implementation exposing the nearest pixel.
 */
internal class RoughFilm(
    resolution: Resolution,
) : AbstractFilm(resolution) {
    // region Film

    override fun expose(position: Vector2, color: Color) {
        if (position.isNaN()) return

        val i = position.x.roundToInt()
        val j = position.y.roundToInt()
        expose(i, j, color, 1.0)
    }

    // endregion
}

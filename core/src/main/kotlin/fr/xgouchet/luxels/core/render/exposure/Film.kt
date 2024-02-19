package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.math.Vector2

/**
 * A Film which can be exposed to luxels, to render a simulation.
 */
internal interface Film {

    val width: Int
    val height: Int

    fun expose(position: Vector2, color: Color)

    fun getColor(i: Int, j: Int): Color
}

package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.collections.zip
import fr.xgouchet.luxels.core.math.geometry.Vector
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

abstract class Curve<V : Vector>(private val points: List<V>) {

    constructor(vararg points: V) : this(listOf(*points))

    /**
     * @param p the progress along the curve (0..1)
     * @return the position on the curve
     */
    fun getPosition(p: Double): V {
        val step = p.coerceIn(0.0, 1.0) * (points.size - 1)

        val i = floor(step).toInt()
        val t = step - i
        val j = min(i + 1, points.size - 1)

        // m · · N --*---- O · · P
        val m = points[max(i - 1, 0)].components()
        val n = points[i].components()
        val o = points[j].components()
        val p = points[min(j + 1, points.size - 1)].components()

        val nt = zip(m, n, o) { prev, current, next -> current + ((next - prev) / 4.0) }
        val ot = zip(n, o, p) { prev, current, next -> current - ((next - prev) / 4.0) }

        val bezier = bezier(n, nt, ot, o, t)
        return getPointFromComponents(bezier)
    }

    abstract fun getPointFromComponents(components: List<Double>): V

    // region Internal

    private fun bezier(
        p0: List<Double>,
        p1: List<Double>,
        p2: List<Double>,
        p3: List<Double>,
        t: Double,
    ): List<Double> {
        val step0P0 = lerp(p0, p1, t)
        val step0P1 = lerp(p1, p2, t)
        val step0P2 = lerp(p2, p3, t)

        val step1P0 = lerp(step0P0, step0P1, t)
        val step1P1 = lerp(step0P1, step0P2, t)

        return lerp(step1P0, step1P1, t)
    }

    private fun lerp(p0: List<Double>, p1: List<Double>, t: Double): List<Double> {
        return p0.zip(p1) { c0, c1 -> c0 + ((c1 - c0) * t) }
    }

    // endregion
}

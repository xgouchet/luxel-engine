package art.luxels.scenes.demo.metaballs

import art.luxels.components.animation.AnimatedVector
import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.imageio.color.HDRColor
import kotlin.time.Duration

class MetaBallsEnvironment(simulationVolume: Volume<D3>, duration: Duration) : BaseEnvironment<D3>(simulationVolume) {

    private val metaBalls: List<MetaBall> = listOf(
        HDRColor.RED,
        HDRColor.YELLOW,
        HDRColor.GREEN,
        HDRColor.TEAL,
        HDRColor.BLUE,
        HDRColor.VIOLET,
    ).map {
        val scale = RndGen.double.uniform() * 4.0 / 10.0
        MetaBall(
            AnimatedVector(duration, List(6) { RndGen.vector3.inVolume(simulationVolume) }),
            simulationVolume.size.length() * scale,
            it,
        )
    }

    // region Environment

    override fun environmentColor(position: Vector<D3>, time: Duration): HDRColor {
        return metaBallsField(position, time)
    }

    // endregion

    // region Internal

    private fun metaBallsField(position: Vector<D3>, time: Duration): HDRColor {
        var colorField = HDRColor.TRANSPARENT
        metaBalls.forEach { mb ->
            val distance = (mb.position.getValue(time) - position).length()
            val scaledDistance = distance / mb.radius
            colorField += mb.color * distanceContribution(scaledDistance)
        }
        return colorField
    }

    private fun distanceContribution(d: Double): Double {
        if (d < 0.0) return 1.0
        if (d > 1.0) return 0.0

        // Electric decay: f(x) = 1 / x

        // 2000 optimization (Ryan Geiss): f(x) = (x⁴ - x² + 0.25)
        // Avoids ∞ for x = 0, but should only be used with x < √2 / 2

        // 2011 optimization (Ken Perlin): f(x) = 1 - (6x⁵ - 15x⁴ + 10x³)
        // Perfectly maps the range [0.0, 1.0] and has zero derivatives on each ends

        return 1.0 - (d * d * d * (d * (d * 6 - 15) + 10))
    }

    // endregion
}

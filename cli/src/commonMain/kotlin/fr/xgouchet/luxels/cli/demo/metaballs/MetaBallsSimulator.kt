package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.cli.demo.DemoLuxel
import fr.xgouchet.luxels.components.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.time.Duration

// https://www.geisswerks.com/ryan/BLOBS/blobs.html
// TODO higher dimensions?
internal class MetaBallsSimulator : Simulator<Dimension.D3, Luxel<Dimension.D3>, Unit> {

    private var metaballs: List<MetaBall> = emptyList()

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        val camOffset = fromSpherical(time.inWholeMilliseconds * PI * 0.0001)
        return PerspectiveProjection(
            simulationSpace,
            filmSpace,
            simulationSpace.center + (camOffset * simulationSpace.size.length()),
        )
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Unit>,
        logger: Logger,
    ) {
        metaballs = List(6) {
            val scale = RndGen.double.uniform() * 4.0 / 10.0
            MetaBall(
                RndGen.vector3.inVolume(simulation.volume),
                simulation.volume.size.length() * scale,
            )
        }
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
    }

    override suspend fun spawnLuxel(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
    ): Luxel<Dimension.D3> {
        val position = RndGen.vector3.inVolume(simulation.volume.expanded(2.0))
        val metaBallField = metaBallField(position)
        val color = max(0.0, (1.0 - (20.0 * abs(1.0 - metaBallField))))

        return DemoLuxel(
            position,
            (HDRColor.RED * color), // + HDRColor.WHITE
        )
    }

    override fun updateLuxel(luxel: Luxel<Dimension.D3>, time: Duration) {
    }

    override fun outputName(): String {
        return "metaballs"
    }

    // endregion

    // region Internal

    private fun metaBallField(position: Vector<Dimension.D3>): Double {
        var field = 0.0
        metaballs.forEach { mb ->
            val distance = (mb.position - position).length()
            val scaledDistance = distance / mb.radius
            field += distanceContribution(scaledDistance)
        }
        return field
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

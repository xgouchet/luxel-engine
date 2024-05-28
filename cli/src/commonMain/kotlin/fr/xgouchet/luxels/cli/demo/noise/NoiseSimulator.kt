package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.cli.demo.DemoLuxel
import fr.xgouchet.luxels.components.noise.NoiseGenerator
import fr.xgouchet.luxels.components.render.projection.Flat2DProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

// TODO debug weird x=y artifact
internal class NoiseSimulator(
    private val noiseSource: NoiseGenerator<Vector<Dimension.D2>, Double>,
) : Simulator<Dimension.D2, DemoLuxel<Dimension.D2>, Unit> {

    private val noiseScale = Vector2(0.001, 0.001)

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D2>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D2> {
        return Flat2DProjection(simulationSpace, filmSpace)
    }

    override fun spawnLuxel(
        simulation: Configuration.Simulation<Dimension.D2>,
        time: Duration,
    ): DemoLuxel<Dimension.D2> {
        val position = RndGen.vector2.inVolume(simulation.space)
        val noise = noiseSource.noise((position * noiseScale))
        val color = HDRColor(noise, noise, noise)

        return DemoLuxel(position, color)
    }

    override fun outputName(): String {
        return "noise"
    }

    // endregion
}

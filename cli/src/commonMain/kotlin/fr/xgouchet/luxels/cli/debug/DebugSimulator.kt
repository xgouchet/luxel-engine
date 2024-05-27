package fr.xgouchet.luxels.cli.debug

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.noise.DimensionalNoiseGenerator
import fr.xgouchet.luxels.components.noise.PerlinNoiseGenerator
import fr.xgouchet.luxels.components.noise.RidgeNoiseGenerator
import fr.xgouchet.luxels.components.noise.wrapper.VectorToDoubleNoiseGenerator
import fr.xgouchet.luxels.components.render.projection.FlatXYProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class DebugSimulator : Simulator<Dimension.D3, DebugLuxel, Long> {

    val baseNoise: DimensionalNoiseGenerator = PerlinNoiseGenerator()
    val ridged = RidgeNoiseGenerator(baseNoise)

    val vectorToDouble = VectorToDoubleNoiseGenerator<Dimension.D3>(ridged)

    private val noiseScale = Vector3(0.001, 0.001, 0.001)

    private var noiseOffset = Vector.nul(Dimension.D3)

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        return FlatXYProjection(simulationSpace, filmSpace)
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
        noiseOffset = Vector3(0.0, 0.0, time.inWholeMilliseconds * 0.001)
    }

    override fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): DebugLuxel {
        val position = RndGen.vector3.inVolume(simulation.space)
//        val noise = vectorToVector.noise(position * noiseScale)
//        val color = Color(noise.x, noise.y, noise.z)
        val noise = vectorToDouble.noise((position * noiseScale) + noiseOffset)
        val color = HDRColor(noise, noise, noise)

        return DebugLuxel(
            position,
            color,
        )
    }

    override fun outputName(): String {
        return "debug"
    }

    override fun updateLuxel(luxel: DebugLuxel, time: Duration) {
    }

    // endregion
}

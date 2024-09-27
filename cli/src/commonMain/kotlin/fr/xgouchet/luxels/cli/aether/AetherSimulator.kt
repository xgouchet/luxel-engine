package fr.xgouchet.luxels.cli.aether

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.components.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.PI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class AetherSimulator(
    private val luxelLifespan: Int = 0x1000,
) : Simulator<Dimension.D3, AetherLuxel, Long> {

    private var gaussianRange = 0
    private var curves: List<Curve<Dimension.D3>> = emptyList()
    private var successiveStep = 0.0001
    private var frameCenterP: Double = 0.0

    // region Simulator

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Long>,
        logger: Logger,
    ) {
        super.initEnvironment(simulation, inputData, logger)
        gaussianRange = (simulation.quality.count shr 3).toInt()
        successiveStep = 0.1 / (simulation.quality.count shr 2)

        val curveCount = RndGen.int.inRange(5, 8)
        val pointCount = RndGen.int.inRange(4, 8)
        curves = List(curveCount) {
            Curve(List(pointCount) { RndGen.vector3.inVolume(simulation.volume) })
        }
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
        super.onFrameStart(simulation, time, animationDuration)
        frameCenterP = if (animationDuration > 16.milliseconds) {
            (time / animationDuration)
        } else {
            RndGen.double.inRange(0.25, 0.75)
        }
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): AetherLuxel {
        val offsetP = RndGen.int.gaussian(0, gaussianRange) * successiveStep
        val p = frameCenterP + offsetP

        val t = (p * WL_RANGE) + WL_MIN
        val curve = Curve(curves.map { it.getPosition(p) })
        return AetherLuxel(curve, t, luxelLifespan)
    }

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        val angle = (time.inWholeMilliseconds / 1000.0) * (PI / 2.0)
        val offset = fromSpherical(angle, 0.0, simulationSpace.size.length())
        return PerspectiveProjection(
            simulationSpace,
            filmSpace,
            simulationSpace.center + offset,
            simulationSpace.center,
            fov = 70.0,
        )
    }

    override fun outputName(): String {
        return "aether"
    }

    // endregion

    companion object {
        private const val WL_MIN = EMSColorSource.MIN_UV_LIGHT
        private const val WL_RANGE = EMSColorSource.MAX_IR_LIGHT - EMSColorSource.MIN_UV_LIGHT
    }
}

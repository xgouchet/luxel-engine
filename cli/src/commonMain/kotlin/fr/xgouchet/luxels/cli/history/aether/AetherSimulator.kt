package fr.xgouchet.luxels.cli.history.aether

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.geometry.Curve3
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.geometry.Space2
import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inBox
import fr.xgouchet.luxels.core.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.math.PI
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class AetherSimulator : Simulator<AetherLuxel, Long> {

    private var gaussianRange = 0
    private var curves: List<Curve3> = emptyList()
    private var luxelLifespan = 1024
    private var successiveStep = 0.0001
    private var frameCenterP: Double = 0.0

    // region Simulator

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Long>) {
        super.initEnvironment(simulation, inputData)
        luxelLifespan = 0x2000
        gaussianRange = (simulation.luxelPerThread / 2).toInt()
        successiveStep = 0.1 / simulation.luxelPerThread

        val curveCount = RndGen.int.inRange(5, 8)
        val pointCount = RndGen.int.inRange(4, 8)
        curves = List(curveCount) {
            Curve3(List(pointCount) { RndGen.vector3.inBox(simulation.space) })
        }
    }

    override fun onFrameStart(simulation: Configuration.Simulation, time: Duration, animationDuration: Duration) {
        super.onFrameStart(simulation, time, animationDuration)
        if (animationDuration > 16.milliseconds) {
            frameCenterP = (time / animationDuration)
        } else {
            frameCenterP = RndGen.double.inRange(0.25, 0.75)
        }
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): AetherLuxel {
        val offsetP = RndGen.int.gaussian(0, gaussianRange) * successiveStep
        val p = frameCenterP + offsetP

        val t = (p * WL_RANGE) + WL_MIN
        val curve = Curve3(curves.map { it.getPosition(p) })
        return AetherLuxel(curve, t, luxelLifespan)
    }

    override fun getProjection(simulationSpace: Space3, filmSpace: Space2, time: Duration): Projection {
        val angle = (time.inWholeMilliseconds / 1000.0) * (PI / 2.0)
        val offset = Vector3.fromSpherical(angle, 0.0, simulationSpace.size.length())
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

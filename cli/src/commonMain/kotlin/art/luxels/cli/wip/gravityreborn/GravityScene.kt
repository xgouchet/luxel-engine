package art.luxels.cli.wip.gravityreborn

import art.luxels.components.animation.max
import art.luxels.components.projection.base.PerspectiveProjection
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Volume
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GravityScene : Scene<D3, GravityLuxel, Long, GravityEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D3>,
        inputData: InputData<Long>,
        duration: Duration,
    ): GravityEnvironment {
        val simDuration = max(duration, 1.milliseconds)
        return GravityEnvironment(simulationVolume, simDuration)
    }

    override fun getProjection(
        simulationVolume: Volume<D3>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D3> {
        val a = simulationVolume.corner(7)
        val b = simulationVolume.corner(6)
        val position = a + ((b - a) * frameInfo.progression)
        val k = 1.5

        return PerspectiveProjection(
            simulationVolume = simulationVolume,
            filmSpace = filmSpace,
            cameraPosition = (position * k) + (simulationVolume.center * (1.0 - k)),
            targetPosition = simulationVolume.center,
            settings = PerspectiveProjection.Settings(
                fov = 90.0,
                dofFocusDistance = simulationVolume.size.length(),
                dofStrength = 10.0,
                dofSamples = 0,
            ),
        )
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D3, GravityLuxel, GravityEnvironment> {
        return GravitySimulator()
    }

    override fun outputName(): String {
        return "Gravity"
    }

    // endregion
}

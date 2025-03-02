package art.luxels.cli.series.s03gravity

import art.luxels.components.animation.max
import art.luxels.components.projection.base.Flat2DProjection
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GravityScene : Scene<D2, GravityLuxel, Long, GravityEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        inputData: InputData<Long>,
        duration: Duration,
    ): GravityEnvironment {
        val simDuration = max(duration, 1.milliseconds)
        return GravityEnvironment(simulationVolume, simDuration)
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D2, GravityLuxel, GravityEnvironment> {
        return GravitySimulator()
    }

    override fun outputName(): String {
        return "Gravity"
    }

    // endregion
}

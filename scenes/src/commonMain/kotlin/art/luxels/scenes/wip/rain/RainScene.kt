package art.luxels.scenes.wip.rain

import art.luxels.components.projection.base.Flat2DProjection
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class RainScene : Scene<D2, RainLuxel, Long, RainEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        inputData: InputData<Long>,
        duration: Duration,
    ): RainEnvironment {
        return RainEnvironment(simulationVolume)
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D2, RainLuxel, RainEnvironment> {
        return RainSimulator()
    }

    override fun outputName(): String {
        return "rain"
    }

    // endregion
}

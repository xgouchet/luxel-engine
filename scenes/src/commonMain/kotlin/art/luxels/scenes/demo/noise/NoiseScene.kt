package art.luxels.scenes.demo.noise

import art.luxels.components.engine.StaticLuxel
import art.luxels.components.projection.base.Flat2DProjection
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class NoiseScene : Scene<D2, StaticLuxel<D2>, Unit, NoiseEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        inputData: InputData<Unit>,
        duration: Duration,
    ): NoiseEnvironment {
        return NoiseEnvironment(simulationVolume)
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D2, StaticLuxel<D2>, NoiseEnvironment> {
        return NoiseSimulator()
    }

    override fun outputName(): String {
        return "noise"
    }

    // endregion
}

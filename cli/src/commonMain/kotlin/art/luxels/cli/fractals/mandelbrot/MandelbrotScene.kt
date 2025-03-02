package art.luxels.cli.fractals.mandelbrot

import art.luxels.components.engine.BaseEnvironment
import art.luxels.components.projection.base.Flat2DProjection
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Volume
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class MandelbrotScene : Scene<D2, MandelbrotLuxel, Unit, BaseEnvironment<D2>> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        inputData: InputData<Unit>,
        duration: Duration,
    ): BaseEnvironment<D2> {
        return BaseEnvironment(simulationVolume)
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D2, MandelbrotLuxel, BaseEnvironment<D2>> {
        return MandelbrotSimulator()
    }

    override fun outputName(): String {
        return "mandelbrot"
    }

    // endregion
}

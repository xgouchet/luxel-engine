package art.luxels.scenes.wip.bookworm

import art.luxels.core.math.Dimension
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import okio.Path
import kotlin.time.Duration

class BookwormScene : Scene<D3, BookwormLuxel, Path, Environment<D3>> {
    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D3>,
        inputData: InputData<Path>,
        duration: Duration,
    ): Environment<D3> {
        TODO("getEnvironment")
    }

    override fun getProjection(
        simulationVolume: Volume<D3>,
        filmSpace: Volume<Dimension.D2>,
        frameInfo: FrameInfo,
    ): Projection<D3> {
        TODO("getProjection")
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D3, BookwormLuxel, Environment<D3>> {
        TODO("initSimulator")
    }

    override fun outputName(): String {
        TODO("outputName")
    }

    // endregion
}

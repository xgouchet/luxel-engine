package fr.xgouchet.luxels.cli.series.s02pixie

import fr.xgouchet.luxels.components.projection.base.Flat2DProjection
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import okio.Path
import kotlin.time.Duration

class PixieScene : Scene<D2, PixieLuxel, Path, PixieEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        inputData: InputData<Path>,
        duration: Duration,
    ): PixieEnvironment {
        return PixieEnvironment(inputData.data, simulationVolume)
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        val delegate = Flat2DProjection(simulationVolume, filmSpace)
//        return CombinedProjection(
//            StarBloomProjection(delegate, 4, 2.5, 1),
//            GaussianBloomProjection(delegate, 5.0, 6),
//        )
        return delegate
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D2, PixieLuxel, PixieEnvironment> {
        return PixieSimulator()
    }

    override fun outputName(): String {
        return "pixie"
    }

    // endregion
}

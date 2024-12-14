package fr.xgouchet.luxels.cli.fractals.buddhabrot

import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.components.render.projection.Flat2DProjection
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class BuddhabrotScene : Scene<D2, BuddhabrotLuxel, Unit, BaseEnvironment<D2>> {

    lateinit var environment: BaseEnvironment<D2>

    // region Scene

    override fun prepareScene(
        simulationVolume: Volume<D2>,
        duration: Duration,
        inputData: InputData<Unit>,
    ) {
        environment = BaseEnvironment(simulationVolume)
    }

    override fun getFrameEnvironment(frameInfo: FrameInfo): BaseEnvironment<D2> {
        return environment
    }

    override fun getProjection(
        simulationVolume: Volume<D2>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D2> {
        return Flat2DProjection(simulationVolume, filmSpace)
    }

    override fun initSimulator(): Simulator<D2, BuddhabrotLuxel, BaseEnvironment<D2>> {
        return BuddhabrotSimulator()
    }

    override fun outputName(): String {
        return "buddhabrot"
    }

    // endregion
}

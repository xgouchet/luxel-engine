package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.components.projection.base.Flat2DProjection
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class NoiseScene : Scene<D2, StaticLuxel<D2>, Unit, NoiseEnvironment> {

    lateinit var environment: NoiseEnvironment

    // region Scene

    override fun prepareScene(
        simulationVolume: Volume<D2>,
        duration: Duration,
        inputData: InputData<Unit>,
    ) {
        environment = NoiseEnvironment(simulationVolume)
    }

    override fun getFrameEnvironment(frameInfo: FrameInfo): NoiseEnvironment {
        return environment
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

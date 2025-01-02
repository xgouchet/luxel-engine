package fr.xgouchet.luxels.cli.series.s03gravity

import fr.xgouchet.luxels.components.animation.max
import fr.xgouchet.luxels.components.projection.base.Flat2DProjection
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GravityScene : Scene<D2, GravityLuxel, Long, GravityEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D2>,
        duration: Duration,
        inputData: InputData<Long>,
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

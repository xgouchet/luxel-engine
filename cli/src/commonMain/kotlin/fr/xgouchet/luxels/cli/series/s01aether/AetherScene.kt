package fr.xgouchet.luxels.cli.series.s01aether

import fr.xgouchet.luxels.components.projection.base.PerspectiveProjection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.math.PI
import kotlin.time.Duration

class AetherScene(
    val luxelLifespan: Int = 0x10,
) : Scene<Dimension.D3, AetherLuxel, Long, AetherEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<Dimension.D3>,
        inputData: InputData<Long>,
        duration: Duration,
    ): AetherEnvironment {
        return AetherEnvironment(simulationVolume)
    }

    override fun getProjection(
        simulationVolume: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        frameInfo: FrameInfo,
    ): Projection<Dimension.D3> {
        val angle = (frameInfo.time.inWholeMilliseconds / 1000.0) * (PI / 2.0)
        val offset = fromSpherical(angle, 0.0, simulationVolume.size.length())
        return PerspectiveProjection(
            simulationVolume,
            filmSpace,
            simulationVolume.center + offset,
            simulationVolume.center,
            PerspectiveProjection.Settings(fov = 70.0),
        )
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<Dimension.D3, AetherLuxel, AetherEnvironment> {
        return AetherSimulator(luxelLifespan)
    }

    override fun outputName(): String {
        return "aether"
    }

    // endregion
}

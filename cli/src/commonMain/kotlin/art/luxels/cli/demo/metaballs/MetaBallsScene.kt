package art.luxels.cli.demo.metaballs

import art.luxels.components.engine.StaticLuxel
import art.luxels.components.projection.base.PerspectiveProjection
import art.luxels.core.math.Dimension
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Volume
import art.luxels.core.math.fromSpherical
import art.luxels.engine.api.Scene
import art.luxels.engine.api.Simulator
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.math.PI
import kotlin.time.Duration

class MetaBallsScene : Scene<D3, StaticLuxel<D3>, Unit, MetaBallsEnvironment> {
    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D3>,
        inputData: InputData<Unit>,
        duration: Duration,
    ): MetaBallsEnvironment {
        return MetaBallsEnvironment(simulationVolume, duration)
    }

    override fun getProjection(
        simulationVolume: Volume<D3>,
        filmSpace: Volume<Dimension.D2>,
        frameInfo: FrameInfo,
    ): Projection<D3> {
        val camOffset = fromSpherical(frameInfo.time.inWholeMilliseconds * PI * 0.0001)
        return PerspectiveProjection(
            simulationVolume,
            filmSpace,
            simulationVolume.center + (camOffset * simulationVolume.size.length()),
        )
    }

    override fun initSimulator(frameInfo: FrameInfo): Simulator<D3, StaticLuxel<D3>, MetaBallsEnvironment> {
        return MetaBallsSimulator()
    }

    override fun outputName(): String {
        return "metaballs"
    }

    // endregion
}

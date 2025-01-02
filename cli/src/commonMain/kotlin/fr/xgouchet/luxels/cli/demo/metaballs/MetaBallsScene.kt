package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.components.projection.base.PerspectiveProjection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.fromSpherical
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.math.PI
import kotlin.time.Duration

class MetaBallsScene : Scene<D3, StaticLuxel<D3>, Unit, MetaBallsEnvironment> {

    // region Scene

    override fun getEnvironment(
        simulationVolume: Volume<D3>,
        duration: Duration,
        inputData: InputData<Unit>,
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

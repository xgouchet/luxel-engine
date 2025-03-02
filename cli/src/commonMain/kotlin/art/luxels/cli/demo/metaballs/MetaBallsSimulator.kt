package art.luxels.cli.demo.metaballs

import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo

// https://www.geisswerks.com/ryan/BLOBS/blobs.html
// TODO higher dimensions?
class MetaBallsSimulator : Simulator<D3, StaticLuxel<D3>, MetaBallsEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: MetaBallsEnvironment, frameInfo: FrameInfo): StaticLuxel<D3> {
        val position = RndGen.vector3.inVolume(environment.simulationVolume.expanded(2.0))

        val metaBallField = environment.environmentColor(position, frameInfo.time)
//        val color = max(0.0, (1.0 - (20.0 * abs(1.0 - metaBallField))))

        return StaticLuxel(position, metaBallField)
    }

    override fun updateLuxel(
        luxel: StaticLuxel<D3>,
        environment: MetaBallsEnvironment,
        frameInfo: FrameInfo,
    ) {
    }

    // endregion
}

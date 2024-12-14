package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

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

    override fun updateLuxel(luxel: StaticLuxel<D3>) {
    }

    // endregion
}

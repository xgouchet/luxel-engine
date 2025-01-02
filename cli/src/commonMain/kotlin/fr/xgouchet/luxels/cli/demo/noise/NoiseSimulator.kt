package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.random.inVolume
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

class NoiseSimulator : Simulator<D2, StaticLuxel<D2>, NoiseEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: NoiseEnvironment, frameInfo: FrameInfo): StaticLuxel<D2> {
        val position = RndGen.vector2.inVolume(environment.simulationVolume)
        val noise = environment.getNoise(position)
        val color = HDRColor(noise, noise, noise)

        return StaticLuxel(position, color)
    }

    override fun updateLuxel(luxel: StaticLuxel<D2>, environment: NoiseEnvironment, frameInfo: FrameInfo) {
    }

    // endregion
}

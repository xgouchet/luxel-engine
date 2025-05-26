package art.luxels.scenes.demo.noise

import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.random.RndGen
import art.luxels.core.math.random.inVolume
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo
import art.luxels.imageio.color.HDRColor

class NoiseSimulator : Simulator<D2, StaticLuxel<D2>, NoiseEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: NoiseEnvironment, frameInfo: FrameInfo): StaticLuxel<D2> {
        val position = RndGen.vector2.inVolume(environment.simulationVolume)
        val noise = environment.getNoise(position)
        val color = HDRColor(noise, noise, noise)

        return StaticLuxel(position, color)
    }

    override fun updateLuxel(
        luxel: StaticLuxel<D2>,
        environment: NoiseEnvironment,
        frameInfo: FrameInfo,
    ) {
    }

    // endregion
}

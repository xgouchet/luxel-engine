package art.luxels.cli.series.s01aether

import art.luxels.components.color.WLColorSource
import art.luxels.core.math.Dimension
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo

class AetherSimulator(private val luxelLifespan: Int) : Simulator<Dimension.D3, AetherLuxel, AetherEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: AetherEnvironment, frameInfo: FrameInfo): AetherLuxel {
        val progress = RndGen.double.inRange(0.0, 1.0)
        val wavelength = (progress * WL_RANGE) + WL_MIN
        val curve = environment.getCurve(progress)

        return AetherLuxel(curve, wavelength, luxelLifespan)
    }

    override fun updateLuxel(
        luxel: AetherLuxel,
        environment: AetherEnvironment,
        frameInfo: FrameInfo,
    ) {
    }

    // endregion

    companion object {
        private const val WL_MIN = WLColorSource.MIN_UV_LIGHT
        private const val WL_RANGE = WLColorSource.MAX_IR_LIGHT - WLColorSource.MIN_UV_LIGHT
    }
}

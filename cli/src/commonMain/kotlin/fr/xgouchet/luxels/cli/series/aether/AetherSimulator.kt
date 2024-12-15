package fr.xgouchet.luxels.cli.series.aether

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

class AetherSimulator(
    private val luxelLifespan: Int = 0x400,
) : Simulator<Dimension.D3, AetherLuxel, AetherEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: AetherEnvironment, frameInfo: FrameInfo): AetherLuxel {
        val progress = RndGen.double.inRange(0.0, 1.0)
        val wavelength = (progress * WL_RANGE) + WL_MIN
        val curve = environment.getCurve(progress)

        return AetherLuxel(curve, wavelength, luxelLifespan)
    }

    override fun updateLuxel(luxel: AetherLuxel) {
    }

    // endregion

    companion object {
        private const val WL_MIN = EMSColorSource.MIN_UV_LIGHT
        private const val WL_RANGE = EMSColorSource.MAX_IR_LIGHT - EMSColorSource.MIN_UV_LIGHT
    }
}

package fr.xgouchet.luxels.cli.demo.noise

import fr.xgouchet.luxels.cli.common.AbstractSceneCommand
import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputSource

/**
 * [CliktCommand] for the [NoiseScene].
 */
class NoiseCommand : AbstractSceneCommand<D2, StaticLuxel<D2>, Unit, NoiseEnvironment>("noise", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, StaticLuxel<D2>, Unit, NoiseEnvironment> {
        return NoiseScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}

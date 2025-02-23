package fr.xgouchet.luxels.cli.fractals.buddhabrot

import fr.xgouchet.luxels.cli.common.AbstractSceneCommand
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputSource

/**
 * [CliktCommand] for the [BuddhabrotScene].
 */
class BuddhabrotCommand : AbstractSceneCommand<D2, BuddhabrotLuxel, Unit, BaseEnvironment<D2>>("buddhabrot", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, BuddhabrotLuxel, Unit, BaseEnvironment<D2>> {
        return BuddhabrotScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}

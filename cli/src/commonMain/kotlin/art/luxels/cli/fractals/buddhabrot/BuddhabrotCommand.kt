package art.luxels.cli.fractals.buddhabrot

import art.luxels.cli.common.AbstractSceneCommand
import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource

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

package fr.xgouchet.luxels.cli.series.s01aether

import fr.xgouchet.luxels.cli.common.LongInputSceneCommand
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.engine.api.Scene

/**
 * [CliktCommand] for the [AetherScene].
 */
class AetherCommand : LongInputSceneCommand<D3, AetherLuxel, AetherEnvironment>("aether", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, AetherLuxel, Long, AetherEnvironment> {
        return AetherScene()
    }

    // endregion
}

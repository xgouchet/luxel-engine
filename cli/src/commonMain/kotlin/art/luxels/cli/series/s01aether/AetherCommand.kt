package art.luxels.cli.series.s01aether

import art.luxels.cli.common.LongInputSceneCommand
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Scene
import com.github.ajalt.clikt.core.CliktCommand

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

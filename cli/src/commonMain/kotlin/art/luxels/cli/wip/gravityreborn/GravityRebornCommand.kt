package art.luxels.cli.wip.gravityreborn

import art.luxels.cli.common.LongInputSceneCommand
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Scene
import com.github.ajalt.clikt.core.CliktCommand

/**
 * [CliktCommand] for the [GravityScene].
 */
class GravityRebornCommand : LongInputSceneCommand<D3, GravityLuxel, GravityEnvironment>("gravity-reborn", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, GravityLuxel, Long, GravityEnvironment> {
        return GravityScene()
    }

    // endregion
}

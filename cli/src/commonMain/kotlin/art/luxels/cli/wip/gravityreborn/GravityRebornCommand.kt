package art.luxels.cli.wip.gravityreborn

import art.luxels.cli.common.LongInputSceneCommand
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Scene
import art.luxels.scenes.wip.gravityreborn.GravityEnvironment
import art.luxels.scenes.wip.gravityreborn.GravityLuxel
import art.luxels.scenes.wip.gravityreborn.GravityScene
import com.github.ajalt.clikt.core.CliktCommand

/**
 * [CliktCommand] for the [art.luxels.scenes.wip.gravityreborn.GravityScene].
 */
class GravityRebornCommand : LongInputSceneCommand<D3, GravityLuxel, GravityEnvironment>("gravity-reborn", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, GravityLuxel, Long, GravityEnvironment> {
        return GravityScene()
    }

    // endregion
}

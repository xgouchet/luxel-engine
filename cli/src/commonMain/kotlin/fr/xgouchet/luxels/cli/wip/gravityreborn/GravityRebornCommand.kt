package fr.xgouchet.luxels.cli.wip.gravityreborn

import fr.xgouchet.luxels.cli.common.LongInputSceneCommand
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.engine.api.Scene

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

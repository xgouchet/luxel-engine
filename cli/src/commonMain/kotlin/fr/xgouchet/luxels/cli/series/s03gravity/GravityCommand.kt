package fr.xgouchet.luxels.cli.series.s03gravity

import fr.xgouchet.luxels.cli.common.LongInputSceneCommand
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.engine.api.Scene

/**
 * [CliktCommand] for the [GravityScene].
 */
class GravityCommand : LongInputSceneCommand<D2, GravityLuxel, GravityEnvironment>("gravity", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, GravityLuxel, Long, GravityEnvironment> {
        return GravityScene()
    }

    // endregion
}

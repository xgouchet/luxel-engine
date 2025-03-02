package art.luxels.cli.series.s03gravity

import art.luxels.cli.common.LongInputSceneCommand
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene
import com.github.ajalt.clikt.core.CliktCommand

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

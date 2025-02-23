package fr.xgouchet.luxels.cli.series.s02pixie

import fr.xgouchet.luxels.cli.common.PathInputSceneCommand
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.engine.api.Scene
import okio.Path

/**
 * [CliktCommand] for the [PixieScene].
 */
class PixieCommand : PathInputSceneCommand<D2, PixieLuxel, PixieEnvironment>("pixie-dust", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, PixieLuxel, Path, PixieEnvironment> {
        return PixieScene()
    }

    // endregion
}

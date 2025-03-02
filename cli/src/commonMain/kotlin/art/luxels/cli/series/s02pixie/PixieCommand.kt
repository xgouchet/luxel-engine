package art.luxels.cli.series.s02pixie

import art.luxels.cli.common.PathInputSceneCommand
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene
import com.github.ajalt.clikt.core.CliktCommand
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

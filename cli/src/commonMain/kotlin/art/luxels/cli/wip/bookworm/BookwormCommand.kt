package art.luxels.cli.wip.bookworm

import art.luxels.cli.common.PathInputSceneCommand
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Scene
import art.luxels.scenes.wip.bookworm.BookwormLuxel
import art.luxels.scenes.wip.bookworm.BookwormScene
import com.github.ajalt.clikt.core.CliktCommand
import okio.Path

/**
 * [CliktCommand] for the [art.luxels.scenes.wip.gravityreborn.GravityScene].
 */
class BookwormCommand : PathInputSceneCommand<D3, BookwormLuxel, Environment<D3>>("bookworm", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, BookwormLuxel, Path, Environment<D3>> {
        return BookwormScene()
    }

    // endregion
}

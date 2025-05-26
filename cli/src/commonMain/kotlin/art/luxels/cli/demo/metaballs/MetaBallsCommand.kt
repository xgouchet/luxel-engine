package art.luxels.cli.demo.metaballs

import art.luxels.cli.common.AbstractSceneCommand
import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource
import art.luxels.scenes.demo.metaballs.MetaBallsEnvironment
import art.luxels.scenes.demo.metaballs.MetaBallsScene

/**
 * [CliktCommand] for the [art.luxels.scenes.demo.metaballs.MetaBallsScene].
 */
class MetaBallsCommand : AbstractSceneCommand<D3, StaticLuxel<D3>, Unit, MetaBallsEnvironment>("metaballs", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, StaticLuxel<D3>, Unit, MetaBallsEnvironment> {
        return MetaBallsScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}

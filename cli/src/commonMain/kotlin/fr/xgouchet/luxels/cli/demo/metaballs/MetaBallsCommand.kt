package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.cli.common.AbstractSceneCommand
import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputSource

/**
 * [CliktCommand] for the [MetaBallsScene].
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

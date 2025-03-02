package art.luxels.cli.demo.cube

import art.luxels.cli.common.AbstractSceneCommand
import art.luxels.components.engine.BaseEnvironment
import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D3
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource

/**
 * [CliktCommand] for the [CubeScene].
 */
class CubeCommand : AbstractSceneCommand<D3, StaticLuxel<D3>, Unit, BaseEnvironment<D3>>("cube", D3) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D3, StaticLuxel<D3>, Unit, BaseEnvironment<D3>> {
        return CubeScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}

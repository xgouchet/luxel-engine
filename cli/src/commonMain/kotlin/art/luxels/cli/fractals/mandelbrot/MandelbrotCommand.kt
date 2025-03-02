package art.luxels.cli.fractals.mandelbrot

import art.luxels.cli.common.AbstractSceneCommand
import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource
import com.github.ajalt.clikt.core.CliktCommand

/**
 * [CliktCommand] for the [MandelbrotScene].
 */
class MandelbrotCommand : AbstractSceneCommand<D2, MandelbrotLuxel, Unit, BaseEnvironment<D2>>("mandelbrot", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, MandelbrotLuxel, Unit, BaseEnvironment<D2>> {
        return MandelbrotScene()
    }

    override fun getInput(): InputSource<Unit> {
        return InputSource.Default
    }

    // endregion
}

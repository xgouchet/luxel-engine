package fr.xgouchet.luxels.cli.fractals.mandelbrot

import fr.xgouchet.luxels.cli.common.AbstractSceneCommand
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputSource

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

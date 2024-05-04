package fr.xgouchet.luxels.core.configuration.input

import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.fileSystem
import okio.Path

/**
 * Provides a collection of input data for a simulation run.
 * @param D the type of data used as input
 */
abstract class InputSource<D : Any> : Iterable<InputData<D>> {

    /** The list of inputs. */
    protected abstract val inputDataList: List<InputData<D>>

    // region Iterable

    override fun iterator(): Iterator<InputData<D>> = inputDataList.iterator()

    // endregion

    /**
     * A default [InputSource] with a single input data based on [Unit].
     */
    object Default : InputSource<Unit>() {
        override val inputDataList: List<InputData<Unit>> = listOf(
            InputData(
                id = "default",
                seed = 0L,
                data = Unit,
            ),
        )
    }
}

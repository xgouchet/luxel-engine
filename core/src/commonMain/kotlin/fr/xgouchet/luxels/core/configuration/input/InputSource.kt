package fr.xgouchet.luxels.core.configuration.input

/**
 * Provides a collection of input data for a simulation run.
 * @param I the type of data used as input
 */
abstract class InputSource<I : Any> : Iterable<InputData<I>> {

    /** The list of inputs. */
    protected abstract val inputDataList: List<InputData<I>>

    // region Iterable

    override fun iterator(): Iterator<InputData<I>> {
        return inputDataList.iterator()
    }

    // endregion

    /**
     * A default [InputSource] with a single input data based on [Unit].
     */
    object Default : InputSource<Unit>() {
        override val inputDataList: List<InputData<Unit>> = listOf(
            InputData(
                id = "",
                seed = 0L,
                data = Unit,
            ),
        )
    }
}

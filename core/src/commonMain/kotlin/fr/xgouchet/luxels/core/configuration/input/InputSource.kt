package fr.xgouchet.luxels.core.configuration.input

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
                "default",
                0L,
                Unit,
            ),
        )
    }

    //    class Images(dir: String) : InputSource<Source>() {
//
//        override val inputList: List<Input<Source>> = dir.listFiles { file: File ->
//            file.isFile && (file.extension in ImageColorSource.readableImageFileExt)
//        }
//            .orEmpty()
//            .sorted()
//            .map { file ->
//                val seed = file.nameWithoutExtension.hashCode().toLong() xor file.length()
//                Input(
//                    file.nameWithoutExtension,
//                    seed,
//                    file,
//                )
//            }
//    }
}

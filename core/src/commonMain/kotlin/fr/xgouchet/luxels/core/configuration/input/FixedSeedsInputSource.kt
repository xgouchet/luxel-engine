package fr.xgouchet.luxels.core.configuration.input

/**
 * An [InputSource] based on a fixed list of Long numbers.
 * @param seeds the seeds
 */
class FixedSeedsInputSource(vararg seeds: Long) : InputSource<Long>() {
    override val inputDataList: List<InputData<Long>> = seeds.map { seed ->
        InputData(
            seed.toString(16),
            seed,
            seed,
        )
    }
}

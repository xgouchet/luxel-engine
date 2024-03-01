package fr.xgouchet.luxels.core.configuration.input

import kotlinx.datetime.Clock
import kotlin.math.absoluteValue

/**
 * An [InputSource] based on a list of random Long numbers.
 * @param count the number of random numbers (default: 10)
 */
class RandomSeedsInputSource(count: Int = 10) : InputSource<Long>() {

    override val inputDataList: List<InputData<Long>>

    init {
        val now = Clock.System.now()
        inputDataList = List(count) {
            val i = it + 1
            val a = (now.nanosecondsOfSecond.toLong() * i * 1337L)
            val b = (now.epochSeconds * i * 1669)
            val seed = (a xor b).absoluteValue % 4815162342L
            InputData(
                seed.toString(16),
                seed,
                seed,
            )
        }
    }
}

package art.luxels.engine.api.input

import kotlin.math.absoluteValue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * An [InputSource] based on a list of random Long numbers.
 * @param count the number of random numbers (default: 8)
 */
@OptIn(ExperimentalTime::class)
class RandomSeedsInputSource(count: Int = 8) : InputSource<Long>() {
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

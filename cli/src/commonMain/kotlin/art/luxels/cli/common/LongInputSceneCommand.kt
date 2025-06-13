package art.luxels.cli.common

import art.luxels.core.math.Dimension
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.input.FixedSeedsInputSource
import art.luxels.engine.api.input.InputSource
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.varargValues
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue

/**
 *  An abstract [CliktCommand] to run a specific Scene with long numbers as inputs.
 *
 * @param D the dimension of the space Luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the type of the Environment
 *
 * @param name the name of the Scene (and subcommand label)
 * @param dimension the dimension in which the Luxels evolve
 */
abstract class LongInputSceneCommand<D : Dimension, L : Luxel<D>, E : Environment<D>>(name: String, dimension: D) :
    AbstractSceneCommand<D, L, Long, E>(name, dimension) {

    val randomSeeds: Int? by option()
        .int()
        .help("the number of random seeds to use")

    val fixedSeeds by option()
        .long()
        .varargValues()
        .help("the seeds to use (each seed will generate one full simulation)")

    // region AbstractSceneCommand

    final override fun getInput(): InputSource<Long> {
        val seeds = mutableSetOf<Long>()

        randomSeeds?.let { count ->
            val now = Clock.System.now()
            repeat(count) {
                val i = it + 1
                val a = (now.nanosecondsOfSecond.toLong() * i * 1337L)
                val b = (now.epochSeconds * i * 1669)
                val seed = (a xor b).absoluteValue % 4815162342L
                seeds.add(seed)
            }
        }

        fixedSeeds?.let {
            it.forEach { seed -> seeds.add(seed) }
        }

        return FixedSeedsInputSource(seeds)
    }

    // endregion
}

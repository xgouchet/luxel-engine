package art.luxels.components.random

import art.luxels.core.math.random.RndGen

/**
 * Picks a random element from the arguments.
 *
 * @param T the type of elements
 * @param items a list of elements
 * @return one element from the given list
 */
fun <T : Any> RndGen.oneOf(vararg items: T): T {
    return items[int.inRange(0, items.size)]
}

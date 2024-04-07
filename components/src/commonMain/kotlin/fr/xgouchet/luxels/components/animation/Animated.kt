package fr.xgouchet.luxels.components.animation

import kotlin.time.Duration

/**
 * Describes an animated property.
 * @param O the type of the property being animated
 */
interface Animated<O : Any> {

    /**
     * Get the value at the given time.
     */
    fun getValue(time: Duration): O
}

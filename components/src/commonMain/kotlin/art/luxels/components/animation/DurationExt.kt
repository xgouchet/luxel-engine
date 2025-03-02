package art.luxels.components.animation

import kotlin.time.Duration

/**
 * Returns the greater of two durations.
 * @param a a duration
 * @param b a duration
 * @return the greater between a and b.
 */
fun max(a: Duration, b: Duration): Duration {
    return if (a.inWholeNanoseconds > b.inWholeNanoseconds) a else b
}

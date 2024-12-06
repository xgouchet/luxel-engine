package fr.xgouchet.luxels.components.test.ext

import kotlin.time.Duration

fun max(a: Duration, b: Duration): Duration {
    return if (a.inWholeNanoseconds > b.inWholeNanoseconds) a else b
}

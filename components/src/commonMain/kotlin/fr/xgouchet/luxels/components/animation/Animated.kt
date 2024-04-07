package fr.xgouchet.luxels.components.animation

import kotlin.time.Duration

interface Animated<O : Any> {

    fun getValue(time: Duration): O
}

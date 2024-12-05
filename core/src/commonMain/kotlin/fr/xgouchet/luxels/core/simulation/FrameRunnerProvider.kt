package fr.xgouchet.luxels.core.simulation

internal fun interface FrameRunnerProvider<T : Any> {

    fun provide(input: T): FrameRunner
}
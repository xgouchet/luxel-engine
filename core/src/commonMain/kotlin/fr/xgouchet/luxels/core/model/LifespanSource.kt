package fr.xgouchet.luxels.core.model

/**
 * Determines the lifespan of a luxel dynamically.
 */
interface LifespanSource {

    /**
     * Called when a luxel is spawned.
     */
    fun onStart()

    /**
     * Called once per simulation step.
     */
    fun onStep(step: Int)

    /**
     * @return whether the luxel should keep being simulated
     */
    fun isAlive(): Boolean

    /**
     * Called after all the last simulation step is complete.
     */
    fun onEnd()
}

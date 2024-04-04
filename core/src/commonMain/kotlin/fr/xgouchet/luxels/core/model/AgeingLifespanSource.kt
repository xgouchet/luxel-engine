package fr.xgouchet.luxels.core.model

/**
 * A simple [LifespanSource] allowing a fixed number of iterations.
 * @property lifespan the lifespan of the luxel
 */
class AgeingLifespanSource(
    val lifespan: Int,
) : LifespanSource {
    /**
     * The current age of the luxel.
     */
    var age: Int = 0
        private set

    /**
     * The progression of the luxel according to its lifespan.
     */
    var progression: Double = 0.0
        get() = age.toDouble() / lifespan
        private set

    /**
     * Terminates the life of the luxel prematurely.
     */
    fun kill() {
        age = lifespan + 1
    }

    // region LifespanSource

    override fun onStart() {
        age = 0
    }

    override fun onStep(step: Int) {
        age = step
    }

    override fun isAlive(): Boolean {
        return age <= lifespan // Discuss strict inequality ?
    }

    override fun onEnd() {}

    // endregion
}

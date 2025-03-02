package art.luxels.engine.api

import art.luxels.core.math.Dimension
import art.luxels.engine.simulation.runner.FrameInfo

/**
 * The main class describing the behavior of luxels and the environment they're going to be simulated in.
 * The instance is shared across threads when using parallel computation, meaning implementations should be
 * stateless.
 *
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the type of the Environment
 */
interface Simulator<D : Dimension, L : Luxel<D>, E : Environment<D>> {

    /**
     * Spawns a [Luxel] in the simulator.
     * @param environment the simulation environment
     * @param frameInfo the current frame information
     */
    fun spawnLuxel(environment: E, frameInfo: FrameInfo): L

    /**
     * Updates the given [Luxel].
     * This is called repetitively as long as the Luxel is alive. Each call precedes the Luxel
     * contributing to the final image based on its color.
     * @param luxel the [Luxel] to update
     * @param environment the simulation environment
     * @param frameInfo the current frame information
     * @see [Luxel.isAlive]
     */
    fun updateLuxel(
        luxel: L,
        environment: E,
        frameInfo: FrameInfo,
    )
}

package fr.xgouchet.luxels.engine.api

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import kotlin.time.Duration

/**
 * The main class describing the environment luxels be simulated in.
 * The instance is shared across threads by multiple simulators to ensure consistency
 * in the simulation.
 *
 * @param D the dimension of the space luxels evolve in
 */
interface Environment<D : Dimension> {

    /**
     * Called when running the simulation with the [SimulationType.ENV] option, to
     * render a debug visualisation of the simulation's environment.
     * @param position the position in the simulation space
     * @param time the current time within the simulated animation
     * @return the color of the environment based on the given inputs
     */
    fun environmentColor(position: Vector<D>, time: Duration): HDRColor
}

package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3
import fr.xgouchet.luxels.core.render.projection.FlatXYProjection
import fr.xgouchet.luxels.core.render.projection.Projection
import kotlin.time.Duration

/**
 * The main class describing the behavior of luxels and the environment they're going to be simulated in.
 * @param L the type of simulated Luxels
 * @param I the expected Input
 */
interface Simulator<L : Luxel, I : Any> {

    // TODO ensure called always on the main thread !
    /**
     * Called once per input to initialize the environment.
     * @param simulation the simulation options
     * @param inputData the input for the simulation
     */
    fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<I>) {}

    /**
     * Called whenever a projection needs to be created.
     * @param simulationSpace the simulation space
     * @param filmSpace the target film space
     * @param time the current time within the simulated animation
     * @return the type of projection to convert luxel position from simulation space to film space
     */
    fun getProjection(simulationSpace: Space3, filmSpace: Space2, time: Duration): Projection {
        return FlatXYProjection(simulationSpace, filmSpace)
    }

    /**
     * Called once when a new frame starts.
     * @param simulation the simulation options
     * @param time the current time within the simulated animation
     */
    fun onFrameStart(simulation: Configuration.Simulation, time: Duration) {}

    /**
     * Spawns a [Luxel] in the simulator.
     * @param simulation the simulation options
     * @param time the current time within the simulated animation
     */
    fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): L

    /**
     * Updates the given [Luxel].
     * This is called repetitively as long as the Luxel is alive. Each call precedes the Luxel
     * contributing to the final image based on its color.
     * @param luxel the [Luxel] to update
     * @param time the current time within the simulated animation
     * @see [Luxel.isAlive]
     */
    fun updateLuxel(luxel: L, time: Duration) {}

    /**
     * @return the name of this simulator (used to tag output image files)
     */
    fun outputName(): String

    /**
     * Called when running the simulation with the [PassType.ENV] option, to
     * render a debug visualisation of the simulation's environment.
     * @param position the position in the 3D space
     * @param time the current time within the simulated animation
     * @return the color of the environment based on the given inputs
     */
    fun environmentColor(position: Vector3, time: Duration): Color = Color.WHITE
}

package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.projection.Projection
import kotlin.time.Duration

/**
 * The main class describing the behavior of luxels and the environment they're going to be simulated in.
 * @param D the dimension of the space luxels evolve in
 * @param L the type of simulated Luxels
 * @param I the expected Input
 */
interface Simulator<D : Dimension, L : Luxel<D>, I : Any> {
    /**
     * TODO ensure called always on the main thread !
     * Called once per input to initialize the environment.
     * @param simulation the simulation options
     * @param inputData the input for the simulation
     */
    fun initEnvironment(simulation: Configuration.Simulation<D>, inputData: InputData<I>) {}

    /**
     * Called whenever a projection needs to be created.
     * @param simulationSpace the simulation space
     * @param filmSpace the target film space
     * @param time the current time within the simulated animation
     * @return the type of projection to convert luxel position from simulation space to film space
     */
    fun getProjection(
        simulationSpace: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<D>

    /**
     * Called once when a new frame starts.
     * @param simulation the simulation options
     * @param time the current time within the simulated animation
     * @param animationDuration the total duration of the current animation
     */
    fun onFrameStart(
        simulation: Configuration.Simulation<D>,
        time: Duration,
        animationDuration: Duration,
    ) {
    }

    /**
     * Called once when a frame ends.
     * @param time the current time within the simulated animation
     * @param animationDuration the total duration of the current animation
     */
    fun onFrameEnd(time: Duration, animationDuration: Duration) {}

    /**
     * Spawns a [Luxel] in the simulator.
     * @param simulation the simulation options
     * @param time the current time within the simulated animation
     */
    fun spawnLuxel(simulation: Configuration.Simulation<D>, time: Duration): L

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
     * Exposes the given [Luxel] with a lambda.
     * By default the exposition uses the luxel's position and color, but simulators can
     * override this function to apply effects.
     * @param luxel the [Luxel] to expose
     * @param filmExposition a lambda taking the 3D position to expose and the color to use
     */
    fun exposeLuxel(luxel: L, filmExposition: (Vector<D>, Color) -> Unit) {
        filmExposition(luxel.position(), luxel.color())
    }

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
    fun environmentColor(position: Vector<D>, time: Duration): HDRColor {
        return HDRColor.WHITE
    }
}

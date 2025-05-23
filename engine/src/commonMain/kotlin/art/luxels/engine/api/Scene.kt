package art.luxels.engine.api

import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.engine.api.input.InputData
import art.luxels.engine.render.Projection
import art.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

/**
 * Describes a scene to be simulated.
 *
 * @param D the dimension of the space Luxels evolve in
 * @param L the type of simulated Luxels
 * @param I the type of expected Inputs
 * @param E the type of the Environment
 */
interface Scene<D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>> {

    /**
     * Called once per animation to prepare the environment.
     *
     * @param simulationVolume the volume within which the simulation is ran
     * @param inputData the input for the simulation
     * @param duration the duration of the animation
     */
    fun getEnvironment(
        simulationVolume: Volume<D>,
        inputData: InputData<I>,
        duration: Duration,
    ): E

    /**
     * Called whenever a projection needs to be created (called once per frame).
     * @param simulationVolume the volume within which the simulation is ran
     * @param filmSpace the target film space
     * @param frameInfo the current frame information
     * @return the type of projection to convert luxel position from simulation space to film space
     */
    fun getProjection(
        simulationVolume: Volume<D>,
        filmSpace: Volume<Dimension.D2>,
        frameInfo: FrameInfo,
    ): Projection<D>

    /**
     * Called once per thread to initialize a stateless simulator.
     */
    fun initSimulator(frameInfo: FrameInfo): Simulator<D, L, E>

    /**
     * @return the name of this scene (used to tag output image files)
     */
    fun outputName(): String
}

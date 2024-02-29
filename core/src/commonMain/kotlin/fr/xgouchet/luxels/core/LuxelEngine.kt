package fr.xgouchet.luxels.core

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.model.Luxel
import fr.xgouchet.luxels.core.render.exposure.LayeredFilm
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

// TODO use SLF4J or other lib to log instead of println

/**
 * The main engine driving the Luxel simulation.
 */
object LuxelEngine {

//    private val threadPool = ForkJoinPool(
//        Runtime.getRuntime().availableProcessors(),
//        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
//        LuxelEngineCrashHandler(),
//        false,
//    )
//
//    private val outputDir = File("output").apply { mkdirs() }

    /**
     * Runs the simulation, using the provided simulator and configuration.
     * @param L the type of [Luxel] to simulate
     * @param I the type of data used as [InputData] for the simulation
     * @param simulator the simulator determining the behavior of luxels
     * @param configuration the configuration detailing the input, simulation, animation and rendering options
     */
    fun <L : Luxel, I : Any> runSimulation(simulator: Simulator<L, I>, configuration: Configuration<I>) {
        configuration.input.source.forEach {
            runSimulation(simulator, configuration, it)
        }
    }

    // region e

    private fun <L : Luxel, I : Any> runSimulation(
        simulator: Simulator<L, I>,
        configuration: Configuration<I>,
        inputData: InputData<I>,
    ) {
        RndGen.resetSeed(inputData.seed)
        simulator.initEnvironment(configuration.simulation, inputData)

        var frameInfo = FrameInfo(0, 0.seconds)

        while (frameInfo.frameTime <= configuration.animation.duration) {
            println("--- FRAME $frameInfo")
            simulateFrame(simulator, configuration, inputData, frameInfo)
            frameInfo = configuration.animation.increment(frameInfo)
        }
    }

    private fun <L : Luxel, I : Any> simulateFrame(
        simulator: Simulator<L, I>,
        configuration: Configuration<I>,
        inputData: InputData<*>,
        frameInfo: FrameInfo,
    ) {
        val fileName = getFilename(simulator, configuration, inputData, frameInfo)
        val layeredFilm = LayeredFilm(configuration.render.resolution)

        simulateFrameParallel(simulator, configuration, frameInfo, layeredFilm)

        println("  Saving frame -> $fileName")
        configuration.render.fixer.write(layeredFilm, fileName)
    }

    private fun <L : Luxel, I : Any> simulateFrameParallel(
        simulator: Simulator<L, I>,
        configuration: Configuration<I>,
        frameInfo: FrameInfo,
        layeredFilm: LayeredFilm,
    ) {
        simulator.onFrameStart(configuration.simulation, frameInfo.frameTime)
        val threadCount = configuration.simulation.threadCount

//        val countDownLatch = CountDownLatch(threadCount) // TODO use threads join?

        val frameStart = Clock.System.now()

        repeat(threadCount) {
            val worker = configuration.createWorker(simulator, frameInfo) { layer ->
                layeredFilm.mergeLayer(layer)
//                countDownLatch.countDown()
            }

//            threadPool.submit(worker)
        }

//        countDownLatch.await()
        val elapsed = Clock.System.now() - frameStart
        println("\r    ✔ Frame $frameInfo simulation complete in $elapsed")
    }

    private fun getFilename(
        simulator: Simulator<*, *>,
        configuration: Configuration<*>,
        inputData: InputData<*>,
        frameInfo: FrameInfo,
    ): String {
        return buildString {
            append(simulator.outputName())

            append("_")
            append(configuration.render.filmType.name)

            append("_")
            append(inputData.id)

            if (configuration.animation.frameCount > 1) {
                append("_")
                append(frameInfo.frameName())
            }
        }
    }

    // endregion
}

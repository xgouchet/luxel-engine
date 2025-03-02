package art.luxels.cli.common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import art.luxels.core.log.FileLogHandler
import art.luxels.core.log.StdOutLogHandler
import art.luxels.core.log.error
import art.luxels.core.log.info
import art.luxels.core.math.Dimension
import art.luxels.core.math.Volume
import art.luxels.engine.api.Environment
import art.luxels.engine.api.Luxel
import art.luxels.engine.api.Scene
import art.luxels.engine.api.input.InputSource
import art.luxels.engine.simulation.LuxelEngine
import art.luxels.imageio.fileSystem
import okio.FileSystem
import okio.SYSTEM

/**
 *  An abstract [CliktCommand] to run a specific Scene.
 *
 * @param D the dimension of the space Luxels evolve in
 * @param L the type of simulated Luxels
 * @param I the type of expected Inputs
 * @param E the type of the Environment
 *
 * @property name the name of the Scene (and subcommand label)
 * @property dimension the dimension in which the Luxels evolve
 */
abstract class AbstractSceneCommand<D : Dimension, L : Luxel<D>, I : Any, E : Environment<D>>(
    val name: String,
    val dimension: D,
) : CliktCommand(name) {

    val outputPath = baseOutputPath / name

    open val commonOptions by CommonOptions()

    // region AbstractSceneCommand

    abstract fun getScene(): Scene<D, L, I, E>

    abstract fun getInput(): InputSource<I>

    // endregion

    // region CoreCliktCommand

    final override fun run() {
        val logHandler = when (commonOptions.log) {
            LogOutput.STDOUT -> StdOutLogHandler()
            LogOutput.FILE -> FileLogHandler(baseLogPath / "$name.log")
        }

        val cwd = fileSystem.canonicalize(cwdPath)
        logHandler.info("Running <$name> in $cwd")

        val commonConfiguration = commonOptions.asConfiguration(outputPath, logHandler)

        val simulationVolume: Volume<D>? = commonOptions.getSimulationVolume(dimension)

        if (simulationVolume == null) {
            logHandler.error("Missing simulation volume. Use the --density or --volume* options.")
        } else {
            LuxelEngine<D>(dimension, logHandler)
                .runSimulation(
                    getScene(),
                    commonConfiguration,
                    getInput(),
                    simulationVolume,
                )
        }
    }

    // endregion
}

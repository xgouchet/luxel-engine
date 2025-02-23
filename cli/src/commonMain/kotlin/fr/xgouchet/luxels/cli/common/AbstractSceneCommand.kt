package fr.xgouchet.luxels.cli.common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import fr.xgouchet.luxels.core.log.FileLogHandler
import fr.xgouchet.luxels.core.log.StdOutLogHandler
import fr.xgouchet.luxels.core.log.error
import fr.xgouchet.luxels.core.log.info
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.input.InputSource
import fr.xgouchet.luxels.engine.simulation.LuxelEngine
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

        val cwd = FileSystem.SYSTEM.canonicalize(cwdPath)
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

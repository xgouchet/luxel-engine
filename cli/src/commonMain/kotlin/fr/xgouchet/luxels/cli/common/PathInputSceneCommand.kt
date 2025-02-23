package fr.xgouchet.luxels.cli.common

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.Luxel
import fr.xgouchet.luxels.engine.api.input.FilesInputSource
import fr.xgouchet.luxels.engine.api.input.InputSource
import okio.Path
import okio.Path.Companion.toPath

/**
 *  An abstract [CliktCommand] to run a specific Scene with file paths as inputs.
 *
 * @param D the dimension of the space Luxels evolve in
 * @param L the type of simulated Luxels
 * @param E the type of the Environment
 *
 * @param name the name of the Scene (and subcommand label)
 * @param dimension the dimension in which the Luxels evolve
 */
abstract class PathInputSceneCommand<D : Dimension, L : Luxel<D>, E : Environment<D>>(
    name: String,
    dimension: D,
) : AbstractSceneCommand<D, L, Path, E>(name, dimension) {

    val defaultInputDirPath = baseInputPath / name

    val inputDirPath: Path? by option()
        .convert { it.toPath() }
        .help("the input directory path (default is $defaultInputDirPath)")

    // region AbstractSceneCommand

    final override fun getInput(): InputSource<Path> {
        val path = inputDirPath ?: defaultInputDirPath
        return FilesInputSource(path)
    }

    // endregion
}

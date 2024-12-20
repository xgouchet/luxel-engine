package fr.xgouchet.luxels.engine.api.input

import fr.xgouchet.graphikio.fileSystem
import okio.Path

/**
 * An [InputSource] listing all files in a given folder as input.
 */
class FilesInputSource(inputDir: Path) : InputSource<Path>() {

    override val inputDataList: List<InputData<Path>> = fileSystem.list(inputDir).map {
        InputData(
            it.name.substringBeforeLast('.'),
            it.name.hashCode().toLong(),
            it,
        )
    }.sortedBy { it.data.toString() }
}

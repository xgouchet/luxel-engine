package fr.xgouchet.luxels.core.configuration.input

import fr.xgouchet.graphikio.fileSystem
import okio.Path

class FilesInputSource(private val inputDir: Path) : InputSource<Path>() {

    override val inputDataList: List<InputData<Path>> = fileSystem.list(inputDir).map {
        InputData(
            it.name.substringBeforeLast('.'),
            it.name.hashCode().toLong(),
            it,
        )
    }
}
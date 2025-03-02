package fr.xgouchet.luxels.core.system

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

object CpuInfoReader {

    val cpuInfoPath = "/proc/cpuinfo".toPath()

    val processors by lazy { readInfo() }

    private fun readInfo(): List<ProcessorInfo> {
        val processors = mutableListOf<ProcessorInfo>()

        FileSystem.SYSTEM.source(cpuInfoPath).use { fileSource ->
            fileSource.buffer().use { bufferedFileSource ->
                var currentProcessor: ProcessorInfo? = null
                while (true) {
                    val line = bufferedFileSource.readUtf8Line() ?: break
                    val tokens = line.split(':').map { it.trim() }
                    if (tokens.size == 2) {
                        when (tokens[0]) {
                            "processor" -> {
                                if (currentProcessor != null) {
                                    processors.add(currentProcessor)
                                }
                                currentProcessor = ProcessorInfo(
                                    processorId = tokens[1].toIntOrNull() ?: -1,
                                    vendorId = "",
                                    modelName = "",
                                    cores = 1,
                                )
                            }

                            "vendor_id" -> currentProcessor?.let { it.vendorId = tokens[1] }
                            "model name" -> currentProcessor?.let { it.modelName = tokens[1] }
                            "cpu cores" -> currentProcessor?.let { it.cores = tokens[1].toIntOrNull() ?: 0 }
                        }
                    }
                }
                if (currentProcessor != null) {
                    processors.add(currentProcessor)
                }
            }
        }

        return processors
    }


}
package fr.xgouchet.luxels.cli.common

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.transformValues
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.ulong
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.CommonConfiguration
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import okio.Path
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CommonOptions : OptionGroup() {

    val log: LogOutput by option()
        .choice(LogOutput.entries.associate { it.name.lowercase() to it })
        .default(LogOutput.STDOUT)

    val simulationLuxelCount: ULong by option("--quality")
        .ulong()
        .default(0x10uL)
        .help("The number of luxels in the simulation")

    val simulationType: SimulationType by option("--pass")
        .choice(SimulationType.entries.associate { it.name.lowercase() to it })
        .default(SimulationType.RENDER)
        .help("The type of simulation to run")

    val simulationMaxCpuCount: Int by option("--cpu")
        .int()
        .default(4)
        .help("Maximum number of CPU cores")

    val simulationDensity: Double? by option("--density")
        .double()
        .help("The density of the simulation based on the output size")

    val simulationVolume2: Volume<D2>? by option("--volume2")
        .double()
        .transformValues(4) { Volume(Vector2(it[0], it[1]), Vector2(it[2], it[3])) }
        .help("The volume for a 2D simulation")

    val simulationVolume3: Volume<D3>? by option("--volume3")
        .double()
        .transformValues(6) { Volume(Vector3(it[0], it[1], it[2]), Vector3(it[3], it[4], it[5])) }
        .help("The volume for a 3D simulation")

    val animationDuration: Duration by option("--duration")
        .convert { durationConverter(it) }
        .default(0.seconds)
        .help("The duration of the animation")

    val animationFramePerSecond: Int by option("--fps")
        .int()
        .default(24)
        .help("The duration of the animation")

    val outputFilmType: FilmType by option("--output")
        .choice(FilmType.entries.associate { it.name.lowercase() to it })
        .default(FilmType.ROUGH)
        .help("The output film type")

    val outputFilmResolution: Resolution by option("--resolution")
        .choice(Resolution.entries.associate { it.name.lowercase() to it })
        .default(Resolution.XGA)
        .help("The output film resolution")

    // TODO add ISO param?
    val outputFormat: FixerType? by option("--format")
        .choice(mapOf("hdr" to FixerType.HDR, "bmp" to FixerType.BMP(400.0)))
        .help("The output file format")

    fun asConfiguration(outputPath: Path, logHandler: LogHandler): CommonConfiguration {
        return CommonConfiguration(
            simulationLuxelCount = simulationLuxelCount.toLong(),
            simulationMaxThreadCount = simulationMaxCpuCount,
            simulationType = simulationType,
            animationDuration = animationDuration,
            animationFrameStep = (1.seconds / animationFramePerSecond),
            animationFrameInfo = FrameInfo.NULL_FRAME,
            outputFilmType = outputFilmType,
            outputResolution = outputFilmResolution,
            outputFixer = outputFormat?.createFixer(outputPath, logHandler) ?: NoOpFixer(logHandler),
        )
    }

    fun <D : Dimension> getSimulationVolume(dimension: D): Volume<D>? {
        val densityVolume = simulationDensity?.let { density -> getDensityVolume(dimension, density) }
        val customVolume = getCustomVolume(dimension)
        return densityVolume ?: customVolume
    }

    // region Any

    override fun toString(): String {
        return buildString {
            append("CommonOption\n")

            append("\tsimulationLuxelCount: $simulationLuxelCount\n")
            append("\tsimulationMaxCpuCount: $simulationMaxCpuCount\n")
            append("\tsimulationType: $simulationType\n")
            append("\tsimulationDensity: $simulationDensity\n")
            append("\tsimulationVolume2: $simulationVolume2\n")
            append("\tsimulationVolume3: $simulationVolume3\n")

            append("\tanimationDuration: $animationDuration\n")
            append("\tanimationFramePerSecond: $animationFramePerSecond\n")

            append("\toutputFilmType: $outputFilmType\n")
            append("\toutputFilmResolution: $outputFilmResolution\n")
            append("\toutputFixer: $outputFormat\n")
        }
    }

    // endregion

    // region Internal

    private fun <D : Dimension> getDensityVolume(dimension: D, density: Double): Volume<D> {
        return Volume(
            min = Vector.nul(dimension),
            max = Vector.from(dimension) {
                if (it % 2 == 0) {
                    outputFilmResolution.width * density
                } else {
                    outputFilmResolution.height * density
                }
            },
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun <D : Dimension> getCustomVolume(dimension: D): Volume<D>? {
        return when (dimension) {
            is D2 -> simulationVolume2 as? Volume<D>
            is D3 -> simulationVolume3 as? Volume<D>
            else -> TODO()
        }
    }

    // endregion
}

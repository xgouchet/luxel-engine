package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.luxels.cli.bookworm.BookwormInputReader.Companion.missingChars
import fr.xgouchet.luxels.cli.common.baseInputPath
import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFilesFrom
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import kotlin.time.Duration.Companion.seconds

/** Bookworm. */
fun main() {
    val outputPath = baseOutputPath / "bookworm"
    val inputPath = baseInputPath / "bookworm"

    val configuration = configurationWithFilesFrom(Dimension.D3, inputPath) {
        simulation {
            quality(Quality.SAMPLE)

            space(
                Volume(
                    Vector.nul(Dimension.D3),
                    Vector.unit(Dimension.D3),
                ),
            )

            maximumThreadCount(2)
        }

        render {
            resolution(Resolution.SQUARE_2880)
            hdrFixer(path = outputPath)
        }

        animation {
            duration(1.seconds)
            fps(24)
        }
    }

    LuxelEngine.runSimulation(
        simulator = BookwormSimulator(luxelLifespan = 0x1000),
        configuration = configuration,
    )

    println(" >>> Unknown characters: ")
    missingChars.forEach { (k, v) ->
        println(" $k: $v")
    }
}

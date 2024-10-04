package fr.xgouchet.luxels.cli.aether

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import fr.xgouchet.luxels.core.math.Dimension

/** Main. */
fun main() {
    val outputPath = baseOutputPath / "aether"

    val seeds = longArrayOf(
        0xfbfbf1f2,
        0x4ca8c1be,
        0x5b6e7596,
        0x6f3339e7,
        0x7c8a19b4,
        0x10f0e0635,
        0x11db0c4e2,
        0x891ee65a,
        0x1057a0d82,
        0x1123dccb4,
        0x5380ecaa,
        0x6925e91c,
        0x720726ee,
        0xbd3800be,
        0xc413ba81,
        0xd8f657fe,
        0xd4103920,
        0xe40e4ddc,
    )

    @Suppress("SpreadOperator")
    val configuration = configurationWithFixedSeeds(Dimension.D3, *seeds) {
//    val configuration = configurationWithRandomSeeds() {

        simulation {
            quality(Quality.DRAFT)
        }

        render {
            resolution(Resolution.FHD_1080)
            filmType(FilmType.CLEAN)
            bmpFixer(path = outputPath)
        }

        simulationSpaceDensity(2.0)
    }

    LuxelEngine.runSimulation(AetherSimulator(0x100), configuration)
}

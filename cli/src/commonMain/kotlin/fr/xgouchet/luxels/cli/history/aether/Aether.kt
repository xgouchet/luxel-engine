package fr.xgouchet.luxels.cli.history.aether

import fr.xgouchet.luxels.core.LuxelEngine
import fr.xgouchet.luxels.core.configuration.FilmType
import fr.xgouchet.luxels.core.configuration.PassType
import fr.xgouchet.luxels.core.configuration.Quality
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configurationWithFixedSeeds
import fr.xgouchet.luxels.core.configuration.configurationWithRandomSeeds
import okio.Path.Companion.toPath

/** Main. */
fun main() {
    val outputPath = "../output".toPath()

    val seeds = longArrayOf(
//        0xfbfbf1f2,
//        0x4ca8c1be,
//        0x5b6e7596,
//        0x6f3339e7,
//        0x7c8a19b4,
//        0x10f0e0635,
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

    val configuration = configurationWithFixedSeeds(*seeds) {
//    val configuration = configurationWithRandomSeeds() {

        simulation {
            quality(Quality.DRAFT)
            threadCount(10)
            passType(PassType.RENDER)
            space(Resolution.UHD_4K, 2.0)
        }

        render {
            resolution(Resolution.UHD_4K)
            filmType(FilmType.CLEAN)
            hdrFixer(outputPath)
        }

//        animation {
//            duration(1.seconds)
//            fps(100)
//        }
    }

    LuxelEngine.runSimulation(AetherSimulator(), configuration)
}

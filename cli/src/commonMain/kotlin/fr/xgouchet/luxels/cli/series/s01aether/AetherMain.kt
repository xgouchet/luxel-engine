package fr.xgouchet.luxels.cli.series.s01aether

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.bmpFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.configurationWithFixedSeeds

/** Main. */
fun main() {
    val seeds = longArrayOf(
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
        0xfbfbf1f2,
    )

    @Suppress("SpreadOperator")
    AetherScene(0x400) runWith configurationWithFixedSeeds(D3, *seeds) {
        simulation {
            quality(Quality.GRAINY_100)
        }

        render {
            resolution(Resolution.UHD_8K)
            filmType(FilmType.ROUGH)
            bmpFixer(path = baseOutputPath / "aether")
        }

        simulationVolumeDensity(2.0)
    }
}

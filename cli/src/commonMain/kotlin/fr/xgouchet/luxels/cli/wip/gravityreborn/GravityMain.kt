package fr.xgouchet.luxels.cli.wip.gravityreborn

import fr.xgouchet.luxels.cli.common.baseOutputPath
import fr.xgouchet.luxels.components.configuration.hdrFixer
import fr.xgouchet.luxels.components.engine.runWith
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.api.configuration.configurationWithFixedSeeds
import kotlin.time.Duration.Companion.seconds

fun main() {
    GravityScene() runWith configurationWithFixedSeeds(D3, 42L) {
        simulation {
            quality(Quality.PREVIEW)
            passType(SimulationType.RENDER)
            maximumThreadCount(10)
        }

        render {
            resolution(Resolution.UHD_4K)
            filmType(FilmType.ROUGH)
            hdrFixer(path = baseOutputPath / "gravity_reborn")
        }

        animation {
            duration(15.seconds)
            fps(24)
        }

        simulationVolumeDensity(1.0)
    }
}

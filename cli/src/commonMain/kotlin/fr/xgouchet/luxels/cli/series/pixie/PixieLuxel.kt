package fr.xgouchet.luxels.cli.series.pixie

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.components.color.ImageColorSource
import fr.xgouchet.luxels.components.engine.PrincipledLuxel
import fr.xgouchet.luxels.components.position.SimplePositionSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource

class PixieLuxel(
    rasterData: RasterData,
    initialPosition: Vector<Dimension.D2>,
    initialUV: Vector<Dimension.D2>,
    val colorMask: HDRColor,
    lifespan: Int,
) : PrincipledLuxel<Dimension.D2, ImageColorSource, SimplePositionSource<Dimension.D2>, AgeingLifespanSource>(
    ImageColorSource(rasterData, initialUV),
    SimplePositionSource(initialPosition),
    AgeingLifespanSource(lifespan),
) {
    // region Luxel

    override fun color(): HDRColor {
        return colorMask
    }

    // endregion
}

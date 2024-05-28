package fr.xgouchet.luxels.cli.pixie

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.components.color.ImageColorSource
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.SimplePositionSource

internal class PixieLuxel(
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

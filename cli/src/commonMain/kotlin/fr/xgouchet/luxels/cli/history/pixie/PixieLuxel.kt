package fr.xgouchet.luxels.cli.history.pixie

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.components.color.ImageColorSource
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.SimplePositionSource

internal class PixieLuxel(
    rasterData: RasterData,
    initialPosition: Vector3,
    initialUV: Vector2,
    val colorMask: HDRColor,
    lifespan: Int,
) : PrincipledLuxel<ImageColorSource, SimplePositionSource, AgeingLifespanSource>(
    ImageColorSource(rasterData, initialUV),
    SimplePositionSource(initialPosition),
    AgeingLifespanSource(lifespan),
) {

    override fun color(): HDRColor {
        return colorMask
    }
}

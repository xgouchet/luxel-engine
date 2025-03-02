package art.luxels.cli.series.s02pixie

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.model.AgeingLifespanSource
import art.luxels.core.model.LifespanSource
import art.luxels.engine.api.Luxel
import art.luxels.imageio.color.HDRColor

class PixieLuxel(
    val colorMask: HDRColor,
    initialPosition: Vector<Dimension.D2>,
    val lifespanSource: AgeingLifespanSource,
    val originalColor: HDRColor,
) : Luxel<Dimension.D2>, LifespanSource by lifespanSource {

    constructor(
        colorMask: HDRColor,
        initialPosition: Vector<Dimension.D2>,
        lifespan: Int,
        originalColor: HDRColor,
    ) : this(
        colorMask,
        initialPosition,
        AgeingLifespanSource(lifespan),
        originalColor,
    )

    var color: HDRColor = originalColor
    var nextColor: HDRColor = HDRColor.TRANSPARENT
    var position: Vector<Dimension.D2> = initialPosition

    // region PositionSource

    override fun position(): Vector<Dimension.D2> {
        return position
    }

    // endregion

    // region ColorSource

    override fun color(): HDRColor {
        return colorMask // (colorMask * originalColor)// + originalColor
    }

    // endregion

    // region LifespanSource

    override fun onStep(step: Int) {
        lifespanSource.onStep(step)
        color = nextColor
    }

    override fun isAlive(): Boolean {
        return lifespanSource.isAlive()
    }

    // endregion
}

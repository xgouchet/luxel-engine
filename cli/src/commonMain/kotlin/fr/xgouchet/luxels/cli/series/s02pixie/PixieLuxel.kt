package fr.xgouchet.luxels.cli.series.s02pixie

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.LifespanSource
import fr.xgouchet.luxels.engine.api.Luxel

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

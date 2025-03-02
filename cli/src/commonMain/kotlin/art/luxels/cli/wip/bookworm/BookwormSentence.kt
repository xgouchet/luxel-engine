package art.luxels.cli.wip.bookworm

import art.luxels.components.geometry.Curve
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.x
import art.luxels.core.math.y
import kotlin.math.pow

class BookwormSentence(
    val tokens: List<BookwormToken>,
    val sentenceIndex: Int,
) {

    val sentenceLength = tokens.size

    private val positionCurve = Curve(tokens.map { it.getPosition() })

    private val intensityCurve = Curve(tokens.map { it.getIntensity() })

    // region BookwormSentence

    fun getPosition(progression: Double): Vector<Dimension.D3> {
        return positionCurve.getPosition(progression)
    }

    fun getIntensity(progression: Double): Double {
        val position = intensityCurve.getPosition(progression)
        return position.x * position.y.pow(4)
    }

    // endregion

    // region Any

    override fun toString(): String {
        return tokens.joinToString(" ")
    }

    // endregion
}

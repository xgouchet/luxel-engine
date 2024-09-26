package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RandomGenerator
import fr.xgouchet.luxels.core.position.PositionSource

class BookwormPositionSource(
    val rng: RandomGenerator<Vector<Dimension.D3>>,
    val sentence: BookwormSentence,
) : PositionSource<Dimension.D3> {

    var progression: Double = 0.0


    // region PositionSource

    override fun position(): Vector<Dimension.D3> {
        return sentence.getPosition(progression) + (rng.gaussian() * 0.0002)
    }

    // endregion
}
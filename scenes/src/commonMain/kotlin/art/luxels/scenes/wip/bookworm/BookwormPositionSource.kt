package art.luxels.scenes.wip.bookworm

import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.random.RandomGenerator
import art.luxels.core.position.PositionSource

class BookwormPositionSource(val rng: RandomGenerator<Vector<Dimension.D3>>, val sentence: BookwormSentence) :
    PositionSource<Dimension.D3> {

    var progression: Double = 0.0

    // region PositionSource

    override fun position(): Vector<Dimension.D3> {
        return sentence.getPosition(progression) + (rng.gaussian() * 0.0002)
    }

    // endregion
}

package fr.xgouchet.luxels.cli.buddhabrot

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.position.UpdatablePositionSource

internal class BuddhabrotPositionSource(maxSteps: Int) : UpdatablePositionSource<Dimension.D2> {

    val initialPos: Complex

    var complexPosition: Complex = Complex(0.0, 0.0)

    init {
        var startPosition: Complex

        do {
            startPosition = Complex(
                RndGen.double.inRange(-2.0, 2.0),
                RndGen.double.inRange(-2.0, 2.0),
            )
        } while (isInside(startPosition, maxSteps))

        initialPos = startPosition
        complexPosition = startPosition
    }

    // region PositionSource

    override fun position(): Vector<Dimension.D2> {
        return complexPosition.asVector()
    }

    // endregion

    // region UpdatablePositionSource

    override fun updatePosition(strength: Double) {
        complexPosition = mandelbrotIteration(complexPosition, initialPos)
    }

    // endregion

    // region Internal

    private fun isInside(c: Complex, maxSteps: Int): Boolean {
        var z = c
        repeat(maxSteps) {
            z = mandelbrotIteration(z, c)
            if (!isAlive(z)) return false
        }
        return true
    }

    fun isAlive(): Boolean {
        return Companion.isAlive(complexPosition)
    }

    // endregion

    companion object {
        private const val OUTSIDE_MAGNITUDE_THRESHOLD = 25.0

        // region Internal

        internal fun mandelbrotIteration(z: Complex, c: Complex): Complex {
            return (z * z) + c
        }

        internal fun isAlive(z: Complex): Boolean {
            return (z.squaredMagnitude() <= OUTSIDE_MAGNITUDE_THRESHOLD)
        }

        // endregion
    }
}

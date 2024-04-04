package fr.xgouchet.luxels.cli.buddhabrot

import fr.xgouchet.luxels.core.gen.random.RndGen
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space3
import fr.xgouchet.luxels.core.position.UpdatablePositionSource

// DISCUSS: use Z0 = 0.0 ?
class BuddhabrotPositionSource(
    val maxSteps: Int,
    val simulationSpace: Space3,
) : UpdatablePositionSource {

    val initialPos: Vector3

    var complexPosition: Vector3 = Vector3.NULL

    init {
        var vector = Vector3(
            RndGen.double.inRange(-2.0, 2.0),
            RndGen.double.inRange(-2.0, 2.0),
            0.0,
        )

        while (isInside(vector, maxSteps)) {
            vector = Vector3(
                RndGen.double.inRange(-2.0, 2.0),
                RndGen.double.inRange(-2.0, 2.0),
                0.0,
            )
        }

        initialPos = vector
        complexPosition = vector
    }

    override fun position(): Vector3 {
        return Vector3(
            ((complexPosition.x + 2.0) / 4.0) * simulationSpace.size.x,
            ((complexPosition.y + 2.0) / 4.0) * simulationSpace.size.x,
            0.0,
        )
    }

    override fun updatePosition(strength: Double) {
        complexPosition = mandelbrotStep(complexPosition, initialPos)
    }

    private fun isInside(c: Vector3, maxSteps: Int): Boolean {
        var z = c.copy()
        repeat(maxSteps) {
            z = mandelbrotStep(z, c)
            if (z.squaredLength() > 25.0) return false
        }
        return true
    }
}

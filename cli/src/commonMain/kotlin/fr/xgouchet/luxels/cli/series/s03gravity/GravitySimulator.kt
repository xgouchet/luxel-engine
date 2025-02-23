package fr.xgouchet.luxels.cli.series.s03gravity

import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo

class GravitySimulator(
    val deviationStrength: Double = 3.0,
) : Simulator<D2, GravityLuxel, GravityEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: GravityEnvironment, frameInfo: FrameInfo): GravityLuxel {
        return GravityLuxel(environment.simulationVolume, 1024)
    }

    override fun updateLuxel(
        luxel: GravityLuxel,
        environment: GravityEnvironment,
        frameInfo: FrameInfo,
    ) {
        var force = Vector.nul(D2)
        environment.attractors.forEach { attractor ->
            val attractorPos = attractor.position.getValue(frameInfo.time)
            val delta = attractorPos - luxel.position
            val m = delta.length()
            if (m > 10.0) {
                val dir = delta.normalized()
                // add gravity
                force += (dir * attractor.gravity)

                // add Centripetal force
                force += Vector2(-dir.y, dir.x) * attractor.orbit
            } else {
                luxel.lifespanSource.kill()
//                return@forEach
            }
        }

        // add the force to the position
        luxel.position += (force * luxel.lifespanSource.age.toDouble())

        // add deviation
        val deviationFactor = luxel.deviationFactor * luxel.deviationFactor * deviationStrength
        luxel.position += RndGen.vector2.gaussian() * deviationFactor
    }

    // endregion
}

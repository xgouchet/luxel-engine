package art.luxels.scenes.wip.gravityreborn

import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Vector
import art.luxels.core.math.cross
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo

class GravitySimulator(
    val deviationStrength: Double = 0.25,
) : Simulator<D3, GravityLuxel, GravityEnvironment> {

    // region Simulator

    override fun spawnLuxel(environment: GravityEnvironment, frameInfo: FrameInfo): GravityLuxel {
        return GravityLuxel(environment.simulationVolume, 1024)
    }

    override fun updateLuxel(
        luxel: GravityLuxel,
        environment: GravityEnvironment,
        frameInfo: FrameInfo,
    ) {
        var force = Vector.nul(D3)
        environment.attractors.forEach { attractor ->
            val attractorPos = attractor.position.getValue(frameInfo.time)
            val delta = attractorPos - luxel.position
            val m = delta.length()
            if (m > 1.0) {
                // add gravity
                val dir = delta.normalized()
                force += (dir * attractor.gravity)

                // add Centripetal force
                val side = attractor.axis cross dir
                if (!side.isNaN()) {
                    force += side * attractor.orbit
                }
            }
            if (m < 5.0) {
                luxel.lifespanSource.kill()
            }
        }

        // add the force to the position
        luxel.position += (force.normalized() * luxel.lifespanSource.age.toDouble() * 2.0)

        // add deviation
        val deviationFactor = luxel.deviationFactor * luxel.deviationFactor * deviationStrength
        luxel.position += RndGen.vector3.gaussian() * deviationFactor
    }

    // endregion
}

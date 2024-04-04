package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Matrix4x4
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector4
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3

/**
 * A [Projection] using a Perspective 3D camera.
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 * @param cameraPosition the position (in simulation space) of the camera
 * @param targetPosition the position (in simulation space) that the camera is pointed at (defaults to the
 * center of the simulation space)
 * @param fov the field of view angle in degrees (defaults to 90°)
 */
class PerspectiveProjection(
    override val simulationSpace: Space3,
    override val filmSpace: Space2,
    cameraPosition: Vector3,
    targetPosition: Vector3 = simulationSpace.center,
    fov: Double = 90.0,
) : Projection {
    private var viewMatrix: Matrix4x4 = Matrix4x4.IDENTITY
    private var projectionMatrix: Matrix4x4 = Matrix4x4.IDENTITY

    init {
        viewMatrix = Matrix4x4.view(
            cameraPosition,
            targetPosition,
        ).inverse()
        projectionMatrix = Matrix4x4.projection(
            filmSpace.size.x,
            filmSpace.size.y,
            fov * TAU / 360.0,
            0.0,
            simulationSpace.size.length(),
        )
    }

    // region Projection

    override fun convertPosition(position: Vector3): Vector2 {
        val worldPos = position.asPosition()
        val viewPos = viewMatrix * worldPos
        val screenPos = projectionMatrix * viewPos
        val screenPosNormalized = screenPos.xy / screenPos.z
        return (screenPosNormalized * filmSpace.size) + filmSpace.center
    }

    // endregion

    // region Internal

    private fun Vector3.asPosition(): Vector4 {
        return Vector4(x, y, z, 1.0)
    }

    // endregion
}

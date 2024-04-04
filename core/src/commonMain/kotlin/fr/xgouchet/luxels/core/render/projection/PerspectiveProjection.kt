package fr.xgouchet.luxels.core.render.projection

import fr.xgouchet.luxels.core.math.Matrix4x4
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector4
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3

class PerspectiveProjection(
    simulationSpace: Space3,
    filmSpace: Space2,
    cameraPosition: Vector3,
    targetPosition: Vector3 = simulationSpace.center,
    fov: Double = 90.0,
) : AbstractProjection(simulationSpace, filmSpace) {

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
}

fun Vector3.asPosition(): Vector4 {
    return Vector4(x, y, z, 1.0)
}

fun Vector3.asDirection(): Vector4 {
    return Vector4(x, y, z, 0.0)
}

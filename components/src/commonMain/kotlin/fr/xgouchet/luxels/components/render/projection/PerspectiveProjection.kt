package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Matrix
import fr.xgouchet.luxels.core.math.Matrix4x4
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector4
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.asVector
import fr.xgouchet.luxels.core.math.cross
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.xy
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.math.z
import fr.xgouchet.luxels.core.render.projection.Projection
import kotlin.math.tan

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
    override val simulationSpace: Volume<Dimension.D3>,
    override val filmSpace: Volume<Dimension.D2>,
    cameraPosition: Vector<Dimension.D3>,
    targetPosition: Vector<Dimension.D3> = simulationSpace.center,
    fov: Double = 90.0,
) : Projection<Dimension.D3> {

    private var viewMatrix = Matrix.identity(Dimension.D4, Dimension.D4)
    private var projectionMatrix = Matrix.identity(Dimension.D4, Dimension.D4)

    init {
        viewMatrix = view(cameraPosition, targetPosition).inverse()
        projectionMatrix = projection(
            filmSpace.size.x,
            filmSpace.size.y,
            fov * TAU / 360.0,
            0.0,
            simulationSpace.size.length(),
        )
    }

    // region Projection

    override fun convertPosition(position: Vector<Dimension.D3>): Vector<Dimension.D2> {
        val worldPos = position.asPosition()
        val viewPos = viewMatrix * worldPos.asVerticalMatrix()
        val screenPos = (projectionMatrix * viewPos).asVector()
        val screenPosNormalized = screenPos.xy / screenPos.z
        return (screenPosNormalized * filmSpace.size) + filmSpace.center
    }

    // endregion

    // region Internal

    private fun Vector<Dimension.D3>.asPosition(): Vector<Dimension.D4> {
        return Vector4(x, y, z, 1.0)
    }

    // endregion

    companion object {

        internal const val SIZE_4X4 = 16

        /**
         * Creates a Matrix representing the position and orientation of a target camera in a 3D space.
         * @param cameraPosition the position of the camera
         * @param targetPosition the position the camera is pointed at
         */
        fun view(
            cameraPosition: Vector<Dimension.D3>,
            targetPosition: Vector<Dimension.D3>,
        ): Matrix<Dimension.D4, Dimension.D4> {
            val result = Matrix4x4()
            val dir = (targetPosition - cameraPosition).normalized()
            val right = dir cross Vector3(0.0, 1.0, 0.0)
            val up = right cross dir

            result.set(0, 0, right.x)
            result.set(0, 1, right.y)
            result.set(0, 2, right.z)

            result.set(1, 0, up.x)
            result.set(1, 1, up.y)
            result.set(1, 2, up.z)

            result.set(2, 0, dir.x)
            result.set(2, 1, dir.y)
            result.set(2, 2, dir.z)

            result.set(3, 0, cameraPosition.x)
            result.set(3, 1, cameraPosition.y)
            result.set(3, 2, cameraPosition.z)

            result.set(3, 3, 1.0)

            return result
        }

        /**
         * Creates a Matrix representing the projection onto a screen.
         * @param width the width of the screen
         * @param height the height of the screen
         * @param fov the field of view angle (in degrees)
         * @param nearPlane the distance from the camera to the near plane
         * @param farPlane the distance from the camera to the far plane
         */
        fun projection(
            width: Double,
            height: Double,
            fov: Double,
            nearPlane: Double,
            farPlane: Double,
        ): Matrix<Dimension.D4, Dimension.D4> {
            val result = Matrix4x4()
            val aspectRatio = width / height
            val h = 1.0 / tan(fov * 0.5)
            val w = h / aspectRatio
            val depth = farPlane - nearPlane
            val a = farPlane / depth
            val b = (-nearPlane * farPlane) / depth

            result.set(0, 0, w)
            result.set(1, 1, h)
            result.set(2, 2, a)
            result.set(3, 2, b)
            result.set(2, 3, -1.0)

            return result
        }
    }
}

package fr.xgouchet.luxels.components.projection.base

import fr.xgouchet.luxels.components.projection.SinglePositionProjection
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Matrix
import fr.xgouchet.luxels.core.math.Matrix4x4
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Vector4
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.asVector
import fr.xgouchet.luxels.core.math.cross
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.xy
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.math.z
import fr.xgouchet.luxels.engine.render.Projection
import kotlin.math.tan

/**
 * A [Projection] using a Perspective 3D camera.
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 * @param cameraPosition the position (in simulation space) of the camera
 * @param targetPosition the position (in simulation space) that the camera is pointed at (defaults to the
 * center of the simulation volume)
 * @param settings the Settings for the camera effects
 */
class PerspectiveProjection(
    simulationVolume: Volume<D3>,
    filmSpace: Volume<D2>,
    cameraPosition: Vector<D3>,
    targetPosition: Vector<D3> = simulationVolume.center,
    settings: Settings = Settings(),
) : SinglePositionProjection<D3>(simulationVolume, filmSpace) {

    /**
     * The 3D Camera settings.
     *
     * @property fov the field of view angle in degrees (defaults to 90°)
     * @property dofFocusDistance the distance of focus for the Depth of Field effect
     * @property dofStrength the strength of the depth of field effect
     * @property dofSamples the number of samples for the Depth of Field effect
     */
    data class Settings(
        val fov: Double = 90.0,
        val dofFocusDistance: Double = 1.0,
        val dofStrength: Double = 0.1,
        val dofSamples: Int = 0,
    )

    private val viewMatrix = view(cameraPosition, targetPosition).inverse()
    private val dofViewMatrices by lazy {
        val dofCenter = cameraPosition + (targetPosition - cameraPosition).normalized() * settings.dofFocusDistance
        val rng = RndGen.vector3

        List(settings.dofSamples) {
            view(
                cameraPosition = cameraPosition + (rng.gaussian() * settings.dofStrength),
                targetPosition = dofCenter,
            ).inverse()
        }
    }

    private val projectionMatrix = projection(
        filmSpace.size.x,
        filmSpace.size.y,
        settings.fov * TAU / 360.0,
        0.0,
        simulationVolume.size.length(),
    )

    // region Projection

    override fun project(position: Vector<D3>): List<Vector<D2>> {
        val worldPos = position.asPosition()

        return (dofViewMatrices + viewMatrix).map {
            val viewPos = it * worldPos
            val screenPos = (projectionMatrix * viewPos).asVector()
            val screenPosNormalized = screenPos.xy / screenPos.z
            (screenPosNormalized * filmSpace.size) + filmSpace.center
        }
    }

    // endregion

    // region Internal

    private fun Vector<D3>.asPosition(): Matrix<Dimension.D1, Dimension.D4> {
        return Vector4(x, y, z, 1.0).asVerticalMatrix()
    }

    // endregion

    companion object {

        // region Internal

        /**
         * Creates a Matrix representing the position and orientation of a target camera in a 3D space.
         * @param cameraPosition the position of the camera
         * @param targetPosition the position the camera is pointed at
         */
        private fun view(cameraPosition: Vector<D3>, targetPosition: Vector<D3>): Matrix<Dimension.D4, Dimension.D4> {
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
        private fun projection(
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

        // endregion
    }
}

package art.luxels.components.render.projection

import art.luxels.components.projection.SinglePositionProjection
import art.luxels.core.math.Dimension
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.engine.render.Projection

/**
 * TODO
 * A [Projection] using an Equidistant Spherical 3D camera.
 * @param simulationVolume the simulation volume
 * @param filmSpace the film space
 */
class EquidistantSphericalProjection(
    simulationVolume: Volume<Dimension.D3>,
    filmSpace: Volume<Dimension.D2>,
) : SinglePositionProjection<Dimension.D3>(simulationVolume, filmSpace) {

    // region Projection

    override fun project(position: Vector<Dimension.D3>): List<Vector<Dimension.D2>> {
        /*
            PSEUDO CODE
            IN: vec3 point, float fov, float near, float far

            d := 1 / (near - far)
            r := atan(lenght(point.xy), -point.z)  / (fov * 0.5)
            phi := atan(point.y, point.x)

            return vec3 (
                r * cos(phi)
                r * sin(phi)
                (point.z * (near + far) * d +  (2.0 * near * far * d)) / -point.z
            )
         */
        TODO()
    }

    // endregion
}

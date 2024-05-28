package fr.xgouchet.luxels.components.render.projection

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection

/**
 * A [Projection] using an Equidistant Spherical 3D camera.
 * @property simulationSpace the simulation space
 * @property filmSpace the film space
 */
class EquidistantSphericalProjection(
    override val simulationSpace: Volume<Dimension.D3>,
    override val filmSpace: Volume<Dimension.D2>,
) : Projection<Dimension.D3> {

    // region Projection

    override fun convertPosition(position: Vector<Dimension.D3>): Vector<Dimension.D2> {
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

package fr.xgouchet.luxels.core.gen.random

import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space3
import kotlin.math.PI

/**
 * @return a random 3D vector on a circle on the XY plane around the origin of the 3D space
 */
fun RandomGenerator<Vector3>.onUnitCircle(): Vector3 {
    val polar = RndGen.double.inRange(0.0, TAU)
    return Vector3.fromSpherical(polar, 0.0, 1.0)
}

/**
 * @return a random 3D vector on a sphere around the origin of the 3D space
 */
fun RandomGenerator<Vector3>.onUnitSphere(): Vector3 {
    val polar = RndGen.double.inRange(0.0, TAU)
    val azimuth = RndGen.double.inRange(-PI, PI)
    return Vector3.fromSpherical(polar, azimuth, 1.0)
}

// fun RandomGenerator<Vector3>.inGaussianDisc(): Vector3 {
//    val polar = RndGen.double.inRange(0.0, TAU)
//    val radius = abs(RndGen.double.gaussian(0.0, 0.333))
//    return Vector3.fromSpherical(polar, 0.0, radius)
// }
//
// fun RandomGenerator<Vector3>.inGaussianBall(): Vector3 {
//    val polar = RndGen.double.inRange(0.0, TAU)
//    val azimuth = RndGen.double.inRange(-PI, PI)
//    val radius = abs(RndGen.double.gaussian(0.0, 0.333))
//    return Vector3.fromSpherical(polar, azimuth, radius)
// }

/**
 * @param space3 a 3D space
 * @return a random 3D vector within the bounds of the given box
 */
fun RandomGenerator<Vector3>.inBox(space3: Space3): Vector3 {
    return inRange(space3.min, space3.max)
}

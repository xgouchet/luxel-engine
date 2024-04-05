package fr.xgouchet.luxels.core.math.random

import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.math.geometry.Vector3

/**
 * @param space3 a 3D space
 * @return a random 3D vector within the bounds of the given box
 */
fun RandomGenerator<Vector3>.inBox(space3: Space3): Vector3 {
    return inRange(space3.min, space3.max)
}

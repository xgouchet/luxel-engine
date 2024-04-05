package fr.xgouchet.luxels.cli.buddhabrot

import fr.xgouchet.luxels.core.math.geometry.Vector3

internal fun mandelbrotStep(z: Vector3, c: Vector3): Vector3 {
    return Vector3(
        (z.x * z.x) - (z.y * z.y) + c.x,
        (z.x * z.y * 2.0) + c.y,
        0.0,
    )
}

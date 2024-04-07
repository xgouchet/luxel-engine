package fr.xgouchet.luxels.components.geometry

import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.geometry.Vector4

fun bezier(
    p0: Vector4,
    p1: Vector4,
    p2: Vector4,
    p3: Vector4,
    t: Double,
): Vector4 {
    val step0P0 = lerp(p0, p1, t)
    val step0P1 = lerp(p1, p2, t)
    val step0P2 = lerp(p2, p3, t)

    val step1P0 = lerp(step0P0, step0P1, t)
    val step1P1 = lerp(step0P1, step0P2, t)

    return lerp(step1P0, step1P1, t)
}

fun bezier(
    p0: Vector3,
    p1: Vector3,
    p2: Vector3,
    p3: Vector3,
    t: Double,
): Vector3 {
    val step0P0 = lerp(p0, p1, t)
    val step0P1 = lerp(p1, p2, t)
    val step0P2 = lerp(p2, p3, t)

    val step1P0 = lerp(step0P0, step0P1, t)
    val step1P1 = lerp(step0P1, step0P2, t)

    return lerp(step1P0, step1P1, t)
}

fun bezier(
    p0: Vector2,
    p1: Vector2,
    p2: Vector2,
    p3: Vector2,
    t: Double,
): Vector2 {
    val step0P0 = lerp(p0, p1, t)
    val step0P1 = lerp(p1, p2, t)
    val step0P2 = lerp(p2, p3, t)

    val step1P0 = lerp(step0P0, step0P1, t)
    val step1P1 = lerp(step0P1, step0P2, t)

    return lerp(step1P0, step1P1, t)
}

fun lerp(p0: Vector4, p1: Vector4, p: Double): Vector4 {
    return Vector4(
        p0.x + ((p1.x - p0.x) * p),
        p0.y + ((p1.y - p0.y) * p),
        p0.z + ((p1.z - p0.z) * p),
        p0.w + ((p1.w - p0.w) * p),
    )
}

fun lerp(p0: Vector3, p1: Vector3, p: Double): Vector3 {
    return Vector3(
        p0.x + ((p1.x - p0.x) * p),
        p0.y + ((p1.y - p0.y) * p),
        p0.z + ((p1.z - p0.z) * p),
    )
}

fun lerp(p0: Vector2, p1: Vector2, p: Double): Vector2 {
    return Vector2(
        p0.x + ((p1.x - p0.x) * p),
        p0.y + ((p1.y - p0.y) * p),
    )
}

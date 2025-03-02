package art.luxels.core.math

import kotlin.math.cos
import kotlin.math.sin

// region Init

/** Creates a 1D vector. */
@Suppress("FunctionName")
fun Vector1(x: Double): Vector<Dimension.D1> {
    return Vector(doubleArrayOf(x))
}

/**
 * Creates a 2D vector.
 * @param x the x component
 * @param y the y component
 */
@Suppress("FunctionName")
fun Vector2(x: Double, y: Double): Vector<Dimension.D2> {
    return Vector(doubleArrayOf(x, y))
}

/**
 * Creates a 3D vector.
 * @param x the x component
 * @param y the y component
 * @param z the z component
 */
@Suppress("FunctionName")
fun Vector3(
    x: Double,
    y: Double,
    z: Double,
): Vector<Dimension.D3> {
    return Vector(doubleArrayOf(x, y, z))
}

/**
 * Creates a 4D vector.
 * @param x the x component
 * @param y the y component
 * @param z the z component
 * @param w the w component
 */
@Suppress("FunctionName")
fun Vector4(
    x: Double,
    y: Double,
    z: Double,
    w: Double,
): Vector<Dimension.D4> {
    return Vector(doubleArrayOf(x, y, z, w))
}

// endregion

// region Vector<*> -> Double
// TODO ?  Vector<*> -> Vector<D1>

/** The x component. */
val Vector<*>.x: Double
    get() = this[0]

/** The y component. */
val Vector<*>.y: Double
    get() = this[1]

/** The z component. */
val Vector<*>.z: Double
    get() = this[2]

/** The w component. */
val Vector<*>.w: Double
    get() = this[3]

// endregion

// region Vector<*> -> Vector<D2>

/** A 2D vector with the x and y components. */
val Vector<*>.xy: Vector<Dimension.D2>
    get() = Vector2(x, y)

/** A 2D vector with the x and z components. */
val Vector<*>.xz: Vector<Dimension.D2>
    get() = Vector2(x, z)

/** A 2D vector with the x and w components. */
val Vector<*>.xw: Vector<Dimension.D2>
    get() = Vector2(x, w)

/** A 2D vector with the y and z components. */
val Vector<*>.yz: Vector<Dimension.D2>
    get() = Vector2(y, z)

/** A 2D vector with the y and w components. */
val Vector<*>.yw: Vector<Dimension.D2>
    get() = Vector2(y, w)

/** A 2D vector with the z and w components. */
val Vector<*>.zw: Vector<Dimension.D2>
    get() = Vector2(z, w)

// endregion

// region Vector<*> -> Vector<D3>

/** A 3D vector with the x, y and z components. */
val Vector<*>.xyz: Vector<Dimension.D3>
    get() = Vector3(x, y, z)

/** A 3D vector with the x, y and w components. */
val Vector<*>.xyw: Vector<Dimension.D3>
    get() = Vector3(x, y, w)

/** A 3D vector with the x, z and w components. */
val Vector<*>.xzw: Vector<Dimension.D3>
    get() = Vector3(x, z, w)

/** A 3D vector with the y, z and w components. */
val Vector<*>.yzw: Vector<Dimension.D3>
    get() = Vector3(y, z, w)

// endregion

// region Dimension specific operation

/**
 * @param other the vector with which to compute the cross product
 * @return the cross product between this vector and the given vector
 */
infix fun Vector<Dimension.D3>.cross(other: Vector<Dimension.D3>): Vector<Dimension.D3> {
    return Vector3(
        (y * other.z) - (z * other.y),
        (z * other.x) - (x * other.z),
        (x * other.y) - (y * other.x),
    )
}

// endregion

// region alternate coordinate systems

/**
 * @param polar (between 0 and 2π)
 * @param radius (between 0 and ∞)
 */
fun fromCircular(polar: Double, radius: Double = 1.0): Vector<Dimension.D2> {
    return Vector2(
        radius * cos(polar),
        radius * sin(polar),
    )
}

/**
 * @param polar (between 0 and 2π)
 * @param azimuth (between -π/2 and π/2)
 * @param radius (between 0 and ∞)
 */
fun fromSpherical(
    polar: Double,
    azimuth: Double = 0.0,
    radius: Double = 1.0,
): Vector<Dimension.D3> {
    val cosA = cos(azimuth)
    return Vector3(
        radius * cos(polar) * cosA,
        radius * sin(azimuth),
        radius * sin(polar) * cosA,
    )
}

// endregion

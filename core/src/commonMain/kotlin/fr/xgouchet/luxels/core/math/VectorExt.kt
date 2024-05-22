package fr.xgouchet.luxels.core.math

// region Init

@Suppress("FunctionName")
fun Vector1(x: Double): Vector<Dimension.D1> {
    return Vector(arrayOf(x))
}

@Suppress("FunctionName")
fun Vector2(x: Double, y: Double): Vector<Dimension.D2> {
    return Vector(arrayOf(x, y))
}

@Suppress("FunctionName")
fun Vector3(x: Double, y: Double, z: Double): Vector<Dimension.D3> {
    return Vector(arrayOf(x, y))
}

@Suppress("FunctionName")
fun Vector4(x: Double, y: Double, z: Double, w: Double): Vector<Dimension.D4> {
    return Vector(arrayOf(x, y, z, w))
}

// endregion

// region Vector<*> -> Double
// TODO ?  Vector<*> -> Vector<D1>

val Vector<*>.x: Double
    get() = this[0]
val Vector<*>.y: Double
    get() = this[1]
val Vector<*>.z: Double
    get() = this[2]
val Vector<*>.w: Double
    get() = this[3]

// endregion

// region Vector<*> -> Vector<D2>

val Vector<*>.xy: Vector<Dimension.D2>
    get() = Vector2(x, y)
val Vector<*>.xz: Vector<Dimension.D2>
    get() = Vector2(x, z)
val Vector<*>.xw: Vector<Dimension.D2>
    get() = Vector2(x, w)
val Vector<*>.yz: Vector<Dimension.D2>
    get() = Vector2(y, z)
val Vector<*>.yw: Vector<Dimension.D2>
    get() = Vector2(y, w)
val Vector<*>.zw: Vector<Dimension.D2>
    get() = Vector2(z, w)

// endregion

// region Vector<*> -> Vector<D3>

val Vector<*>.xyz: Vector<Dimension.D3>
    get() = Vector3(x, y, z)
val Vector<*>.xyw: Vector<Dimension.D3>
    get() = Vector3(x, y, w)
val Vector<*>.xzw: Vector<Dimension.D3>
    get() = Vector3(x, z, w)
val Vector<*>.yzw: Vector<Dimension.D3>
    get() = Vector3(y, z, w)

// endregion

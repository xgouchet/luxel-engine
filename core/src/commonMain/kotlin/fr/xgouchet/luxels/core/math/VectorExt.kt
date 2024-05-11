package fr.xgouchet.luxels.core.math

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
    get() = Vector.`2D`(x, y)
val Vector<*>.xz: Vector<Dimension.D2>
    get() = Vector.`2D`(x, z)
val Vector<*>.xw: Vector<Dimension.D2>
    get() = Vector.`2D`(x, w)
val Vector<*>.yz: Vector<Dimension.D2>
    get() = Vector.`2D`(y, z)
val Vector<*>.yw: Vector<Dimension.D2>
    get() = Vector.`2D`(y, w)
val Vector<*>.zw: Vector<Dimension.D2>
    get() = Vector.`2D`(z, w)

// endregion

// region Vector<*> -> Vector<D3>

val Vector<*>.xyz: Vector<Dimension.D3>
    get() = Vector.`3D`(x, y, z)
val Vector<*>.xyw: Vector<Dimension.D3>
    get() = Vector.`3D`(x, y, w)
val Vector<*>.xzw: Vector<Dimension.D3>
    get() = Vector.`3D`(x, z, w)
val Vector<*>.yzw: Vector<Dimension.D3>
    get() = Vector.`3D`(y, z, w)

// endregion

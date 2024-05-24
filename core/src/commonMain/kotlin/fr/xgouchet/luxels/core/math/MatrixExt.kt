@file:Suppress("TooManyFunctions", "FunctionNaming")

package fr.xgouchet.luxels.core.math

// region Init

/** Creates a 1×1 matrix. */
fun Matrix1x1(): Matrix<Dimension.D1, Dimension.D1> {
    return Matrix(Dimension.D1, Dimension.D1)
}

/** Creates a 1×2 matrix. */
fun Matrix1x2(): Matrix<Dimension.D1, Dimension.D2> {
    return Matrix(Dimension.D1, Dimension.D2)
}

/** Creates a 1×3 matrix. */
fun Matrix1x3(): Matrix<Dimension.D1, Dimension.D3> {
    return Matrix(Dimension.D1, Dimension.D3)
}

/** Creates a 1×4 matrix. */
fun Matrix1x4(): Matrix<Dimension.D1, Dimension.D4> {
    return Matrix(Dimension.D1, Dimension.D4)
}

/** Creates a 2×1 matrix. */
fun Matrix2x1(): Matrix<Dimension.D2, Dimension.D1> {
    return Matrix(Dimension.D2, Dimension.D1)
}

/** Creates a 2×2 matrix. */
fun Matrix2x2(): Matrix<Dimension.D2, Dimension.D2> {
    return Matrix(Dimension.D2, Dimension.D2)
}

/** Creates a 2×3 matrix. */
fun Matrix2x3(): Matrix<Dimension.D2, Dimension.D3> {
    return Matrix(Dimension.D2, Dimension.D3)
}

/** Creates a 2×4 matrix. */
fun Matrix2x4(): Matrix<Dimension.D2, Dimension.D4> {
    return Matrix(Dimension.D2, Dimension.D4)
}

/** Creates a 3×1 matrix. */
fun Matrix3x1(): Matrix<Dimension.D3, Dimension.D1> {
    return Matrix(Dimension.D3, Dimension.D1)
}

/** Creates a 3×2 matrix. */
fun Matrix3x2(): Matrix<Dimension.D3, Dimension.D2> {
    return Matrix(Dimension.D3, Dimension.D2)
}

/** Creates a 3×3 matrix. */
fun Matrix3x3(): Matrix<Dimension.D3, Dimension.D3> {
    return Matrix(Dimension.D3, Dimension.D3)
}

/** Creates a 3×4 matrix. */
fun Matrix3x4(): Matrix<Dimension.D3, Dimension.D4> {
    return Matrix(Dimension.D3, Dimension.D4)
}

/** Creates a 4×1 matrix. */
fun Matrix4x1(): Matrix<Dimension.D4, Dimension.D1> {
    return Matrix(Dimension.D4, Dimension.D1)
}

/** Creates a 4×1 matrix. */
fun Matrix4x2(): Matrix<Dimension.D4, Dimension.D2> {
    return Matrix(Dimension.D4, Dimension.D2)
}

/** Creates a 4×3 matrix. */
fun Matrix4x3(): Matrix<Dimension.D4, Dimension.D3> {
    return Matrix(Dimension.D4, Dimension.D3)
}

/** Creates a 4×4 matrix. */
fun Matrix4x4(): Matrix<Dimension.D4, Dimension.D4> {
    return Matrix(Dimension.D4, Dimension.D4)
}

// endregion

// region Conversion to vector

/**
 * Converts a 1×n matrix to an n-dimensional Vector.
 */
fun <D : Dimension> Matrix<Dimension.D1, D>.asVector(): Vector<D> {
    return Vector(data)
}

// endregion

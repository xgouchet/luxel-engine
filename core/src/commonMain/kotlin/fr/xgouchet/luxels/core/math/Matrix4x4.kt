package fr.xgouchet.luxels.core.math

import kotlin.math.tan

/**
 * A basic 4⨉4 Matrix.
 */
class Matrix4x4 internal constructor(
    internal val data: DoubleArray = DoubleArray(SIZE_4X4) { 0.0 },
) {
    init {
        check(data.size == SIZE_4X4)
    }

    /**
     * Inverse the Matrix.
     */
    @Suppress("LongMethod")
    fun inverse(): Matrix4x4 {
        val inv = DoubleArray(SIZE_4X4)

        inv[0] = data[5] * data[10] * data[15] -
            data[5] * data[11] * data[14] -
            data[9] * data[6] * data[15] +
            data[9] * data[7] * data[14] +
            data[13] * data[6] * data[11] -
            data[13] * data[7] * data[10]

        inv[4] = -data[4] * data[10] * data[15] +
            data[4] * data[11] * data[14] +
            data[8] * data[6] * data[15] -
            data[8] * data[7] * data[14] -
            data[12] * data[6] * data[11] +
            data[12] * data[7] * data[10]

        inv[8] = data[4] * data[9] * data[15] -
            data[4] * data[11] * data[13] -
            data[8] * data[5] * data[15] +
            data[8] * data[7] * data[13] +
            data[12] * data[5] * data[11] -
            data[12] * data[7] * data[9]

        inv[12] = -data[4] * data[9] * data[14] +
            data[4] * data[10] * data[13] +
            data[8] * data[5] * data[14] -
            data[8] * data[6] * data[13] -
            data[12] * data[5] * data[10] +
            data[12] * data[6] * data[9]

        inv[1] = -data[1] * data[10] * data[15] +
            data[1] * data[11] * data[14] +
            data[9] * data[2] * data[15] -
            data[9] * data[3] * data[14] -
            data[13] * data[2] * data[11] +
            data[13] * data[3] * data[10]

        inv[5] = data[0] * data[10] * data[15] -
            data[0] * data[11] * data[14] -
            data[8] * data[2] * data[15] +
            data[8] * data[3] * data[14] +
            data[12] * data[2] * data[11] -
            data[12] * data[3] * data[10]

        inv[9] = -data[0] * data[9] * data[15] +
            data[0] * data[11] * data[13] +
            data[8] * data[1] * data[15] -
            data[8] * data[3] * data[13] -
            data[12] * data[1] * data[11] +
            data[12] * data[3] * data[9]

        inv[13] = data[0] * data[9] * data[14] -
            data[0] * data[10] * data[13] -
            data[8] * data[1] * data[14] +
            data[8] * data[2] * data[13] +
            data[12] * data[1] * data[10] -
            data[12] * data[2] * data[9]

        inv[2] = data[1] * data[6] * data[15] -
            data[1] * data[7] * data[14] -
            data[5] * data[2] * data[15] +
            data[5] * data[3] * data[14] +
            data[13] * data[2] * data[7] -
            data[13] * data[3] * data[6]

        inv[6] = -data[0] * data[6] * data[15] +
            data[0] * data[7] * data[14] +
            data[4] * data[2] * data[15] -
            data[4] * data[3] * data[14] -
            data[12] * data[2] * data[7] +
            data[12] * data[3] * data[6]

        inv[10] = data[0] * data[5] * data[15] -
            data[0] * data[7] * data[13] -
            data[4] * data[1] * data[15] +
            data[4] * data[3] * data[13] +
            data[12] * data[1] * data[7] -
            data[12] * data[3] * data[5]

        inv[14] = -data[0] * data[5] * data[14] +
            data[0] * data[6] * data[13] +
            data[4] * data[1] * data[14] -
            data[4] * data[2] * data[13] -
            data[12] * data[1] * data[6] +
            data[12] * data[2] * data[5]

        inv[3] = -data[1] * data[6] * data[11] +
            data[1] * data[7] * data[10] +
            data[5] * data[2] * data[11] -
            data[5] * data[3] * data[10] -
            data[9] * data[2] * data[7] +
            data[9] * data[3] * data[6]

        inv[7] = data[0] * data[6] * data[11] -
            data[0] * data[7] * data[10] -
            data[4] * data[2] * data[11] +
            data[4] * data[3] * data[10] +
            data[8] * data[2] * data[7] -
            data[8] * data[3] * data[6]

        inv[11] = -data[0] * data[5] * data[11] +
            data[0] * data[7] * data[9] +
            data[4] * data[1] * data[11] -
            data[4] * data[3] * data[9] -
            data[8] * data[1] * data[7] +
            data[8] * data[3] * data[5]

        inv[15] = data[0] * data[5] * data[10] -
            data[0] * data[6] * data[9] -
            data[4] * data[1] * data[10] +
            data[4] * data[2] * data[9] +
            data[8] * data[1] * data[6] -
            data[8] * data[2] * data[5]

        val det = data[0] * inv[0] + data[1] * inv[4] + data[2] * inv[8] + data[3] * inv[12]

        if (det == 0.0) {
            throw IllegalArgumentException("Unable to inverse Matrix, the determinant is null")
        }

        for (i in 0..<SIZE_4X4) {
            inv[i] *= det
        }

        return Matrix4x4(inv)
    }

    // region Operators

    /**
     * @param v the vector to multiply with this matrix
     * @return the product of this matrix with a vector
     */
    operator fun times(v: Vector4): Vector4 {
        return Vector4(
            (v.x * data[0]) + (v.y * data[1]) + (v.z * data[2]) + (v.w * data[3]),
            (v.x * data[4]) + (v.y * data[5]) + (v.z * data[6]) + (v.w * data[7]),
            (v.x * data[8]) + (v.y * data[9]) + (v.z * data[10]) + (v.w * data[11]),
            (v.x * data[12]) + (v.y * data[13]) + (v.z * data[14]) + (v.w * data[15]),
        )
    }

    /**
     * @param m the matrix to multiply with this matrix
     * @return the product of this matrix with another matrix
     */
    operator fun times(m: Matrix4x4): Matrix4x4 {
        val mult = DoubleArray(SIZE_4X4)

        mult[0] = (data[0] * m.data[0]) + (data[1] * m.data[4]) + (data[2] * m.data[8]) + (data[3] * m.data[12])
        mult[4] = (data[4] * m.data[0]) + (data[5] * m.data[4]) + (data[6] * m.data[8]) + (data[7] * m.data[12])
        mult[8] = (data[8] * m.data[0]) + (data[9] * m.data[4]) + (data[10] * m.data[8]) + (data[11] * m.data[12])
        mult[12] = (data[12] * m.data[0]) + (data[13] * m.data[4]) + (data[14] * m.data[8]) + (data[15] * m.data[12])

        mult[1] = (data[0] * m.data[1]) + (data[1] * m.data[5]) + (data[2] * m.data[9]) + (data[3] * m.data[13])
        mult[5] = (data[4] * m.data[1]) + (data[5] * m.data[5]) + (data[6] * m.data[9]) + (data[7] * m.data[13])
        mult[9] = (data[8] * m.data[1]) + (data[9] * m.data[5]) + (data[10] * m.data[9]) + (data[11] * m.data[13])
        mult[13] = (data[12] * m.data[1]) + (data[13] * m.data[5]) + (data[14] * m.data[9]) + (data[15] * m.data[13])

        mult[2] = (data[0] * m.data[2]) + (data[1] * m.data[6]) + (data[2] * m.data[10]) + (data[3] * m.data[14])
        mult[6] = (data[4] * m.data[2]) + (data[5] * m.data[6]) + (data[6] * m.data[10]) + (data[7] * m.data[14])
        mult[10] = (data[8] * m.data[2]) + (data[9] * m.data[6]) + (data[10] * m.data[10]) + (data[11] * m.data[14])
        mult[14] = (data[12] * m.data[2]) + (data[13] * m.data[6]) + (data[14] * m.data[10]) + (data[15] * m.data[14])

        mult[3] = (data[0] * m.data[3]) + (data[1] * m.data[7]) + (data[2] * m.data[11]) + (data[3] * m.data[15])
        mult[7] = (data[4] * m.data[3]) + (data[5] * m.data[7]) + (data[6] * m.data[11]) + (data[7] * m.data[15])
        mult[11] = (data[8] * m.data[3]) + (data[9] * m.data[7]) + (data[10] * m.data[11]) + (data[11] * m.data[15])
        mult[15] = (data[12] * m.data[3]) + (data[13] * m.data[7]) + (data[14] * m.data[11]) + (data[15] * m.data[15])

        return Matrix4x4(mult)
    }

    // endregion

    companion object {
        internal const val SIZE_4X4 = 16

        /** The Identity Matrix (all 0s, except 1s on the main diagonal). */
        val IDENTITY: Matrix4x4 = Matrix4x4(
            DoubleArray(SIZE_4X4).apply {
                this[0] = 1.0
                this[5] = 1.0
                this[10] = 1.0
                this[15] = 1.0
            },
        )

        /**
         * Creates a Matrix representing the position and orientation of a target camera in a 3D space.
         * @param cameraPosition the position of the camera
         * @param targetPosition the position the camera is pointed at
         */
        fun view(cameraPosition: Vector3, targetPosition: Vector3): Matrix4x4 {
            val dir = (targetPosition - cameraPosition).normalized()
            val right = dir cross Vector3.Y_AXIS
            val up = right cross dir

            val data = DoubleArray(SIZE_4X4) { 0.0 }
            data[0] = right.x
            data[1] = up.x
            data[2] = dir.x
            data[3] = cameraPosition.x

            data[4] = right.y
            data[5] = up.y
            data[6] = dir.y
            data[7] = cameraPosition.y

            data[8] = right.z
            data[9] = up.z
            data[10] = dir.z
            data[11] = cameraPosition.z

            data[15] = 1.0

            return Matrix4x4(data)
        }

        /**
         * Creates a Matrix representing the projection onto a screen.
         * @param width the width of the screen
         * @param height the height of the screen
         * @param fov the field of view angle (in degrees)
         * @param nearPlane the distance from the camera to the near plane
         * @param farPlane the distance from the camera to the far plane
         */
        fun projection(width: Double, height: Double, fov: Double, nearPlane: Double, farPlane: Double): Matrix4x4 {
            val aspectRatio = width / height
            val w = 1.0 / tan(fov * 0.5)
            val h = w / aspectRatio
            val depth = farPlane - nearPlane
            val a = farPlane / depth
            val b = (-nearPlane * farPlane) / depth

            val data = DoubleArray(SIZE_4X4) { 0.0 }

            data[0] = w
            data[5] = h
            data[10] = a
            data[11] = b
            data[14] = -1.0

            return Matrix4x4(data)
        }
    }
}

/**
 * @param m the matrix to multiply with this vector
 * @return the product of this vector with a matrix
 */
operator fun Vector4.times(m: Matrix4x4): Vector4 {
    return Vector4(
        (x * m.data[0]) + (y * m.data[4]) + (z * m.data[8]) + (w * m.data[12]),
        (x * m.data[1]) + (y * m.data[5]) + (z * m.data[9]) + (w * m.data[13]),
        (x * m.data[2]) + (y * m.data[6]) + (z * m.data[10]) + (w * m.data[14]),
        (x * m.data[3]) + (y * m.data[7]) + (z * m.data[11]) + (w * m.data[15]),
    )
}

package fr.xgouchet.luxels.core.math

/**
 * The dimension of a Euclidean space.
 * @property size the size of the space dimension
 */
sealed class Dimension(val size: Int) {

    /** A range to iterate over all axes of the dimension. */
    val range = 0..<size

    /** 1D. */
    data object D1 : Dimension(1)

    /** 2D. */
    data object D2 : Dimension(2)

    /** 3D. */
    data object D3 : Dimension(3)

    /** 4D. */
    data object D4 : Dimension(4)

    // TODO add D5, D6, … ?
}

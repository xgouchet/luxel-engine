package fr.xgouchet.luxels.core.math

sealed class Dimension(val size: Int) {

    val range = 0..<size

    data object D1 : Dimension(1)

    data object D2 : Dimension(2)

    data object D3 : Dimension(3)

    data object D4 : Dimension(4)

    data object D5 : Dimension(5)

    data object D6 : Dimension(6)
}

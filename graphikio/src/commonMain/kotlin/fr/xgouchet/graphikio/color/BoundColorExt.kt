package fr.xgouchet.graphikio.color

fun BoundColor(
    r: Double,
    g: Double,
    b: Double,
    a: Double = 255.0,
) = BoundColor.bindInRange(r, g, b, a)

fun BoundColor(
    r: Int,
    g: Int,
    b: Int,
    a: Int = 255,
) = BoundColor.bindInRange(r, g, b, a)

package fr.xgouchet.graphikio.color

/**
 * Creates an SDRColor instance from double numbers.
 */
@Suppress("FunctionNaming")
fun SDRColor(
    r: Double,
    g: Double,
    b: Double,
    a: Double = 255.0,
) = SDRColor.bindInRange(r, g, b, a)

/**
 * Creates an SDRColor instance from int numbers.
 */
@Suppress("FunctionNaming")
fun SDRColor(
    r: Int,
    g: Int,
    b: Int,
    a: Int = 255,
) = SDRColor.bindInRange(r, g, b, a)

@file:Suppress("FunctionNaming", "ktlint:standard:function-naming")

package fr.xgouchet.graphikio.color

/**
 * Creates an SDRColor instance from double numbers.
 */
fun SDRColor(
    r: Double,
    g: Double,
    b: Double,
    a: Double = 255.0,
): SDRColor {
    return SDRColor.bindInRange(r, g, b, a)
}

/**
 * Creates an SDRColor instance from int numbers.
 */
fun SDRColor(
    r: Int,
    g: Int,
    b: Int,
    a: Int = 255,
): SDRColor {
    return SDRColor.bindInRange(r, g, b, a)
}

/**
 * Creates an SDRColor instance from byte numbers.
 */
fun SDRColor(
    r: Byte,
    g: Byte,
    b: Byte,
    a: Byte = -1,
): SDRColor {
    return SDRColor.bindInRange(
        if (r >= 0) r.toInt() else (256 + r.toInt()),
        if (g >= 0) g.toInt() else (256 + g.toInt()),
        if (b >= 0) b.toInt() else (256 + b.toInt()),
        if (a >= 0) a.toInt() else (256 + a.toInt()),
    )
}

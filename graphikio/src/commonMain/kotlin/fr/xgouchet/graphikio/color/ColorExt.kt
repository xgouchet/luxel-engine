package fr.xgouchet.graphikio.color

/**
 * Converts this instance into an SDR (Standard Dynamic Range) color.
 */
fun Color.asBoundColor(): SDRColor {
    return when (this) {
        is SDRColor -> this
        is HDRColor -> SDRColor(r, g, b, a)
        else -> throw UnsupportedOperationException("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

/**
 * Converts this instance into an HDR (High Dynamic Range) color.
 */
fun Color.asUnboundColor(): HDRColor {
    return when (this) {
        is HDRColor -> this
        is SDRColor -> HDRColor(r.toDouble(), g.toDouble(), b.toDouble(), a.toDouble())
        else -> throw UnsupportedOperationException("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

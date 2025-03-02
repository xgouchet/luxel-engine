package art.luxels.imageio.color

/**
 * Converts this instance into an SDR (Standard Dynamic Range) color.
 */
fun Color.asSDR(): SDRColor {
    return when (this) {
        is SDRColor -> this
        is HDRColor -> SDRColor(r, g, b, a)
        else -> throw UnsupportedOperationException("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

/**
 * Converts this instance into an HDR (High Dynamic Range) color.
 */
fun Color.asHDR(): HDRColor {
    return when (this) {
        is HDRColor -> this
        is SDRColor -> HDRColor(r.toDouble(), g.toDouble(), b.toDouble(), a.toDouble())
        else -> throw UnsupportedOperationException("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

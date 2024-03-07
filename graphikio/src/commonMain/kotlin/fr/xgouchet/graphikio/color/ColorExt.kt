package fr.xgouchet.graphikio.color

fun Color.asBoundColor(): BoundColor {
    return when (this) {
        is BoundColor -> this
        is UnboundColor -> BoundColor(r, g, b, a)
        else -> TODO("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

fun Color.asUnboundColor(): UnboundColor {
    return when (this) {
        is UnboundColor -> this
        is BoundColor -> UnboundColor(r.toDouble(), g.toDouble(), b.toDouble(), a.toDouble())
        else -> TODO("Unimplemented conversion to BoundColor from type ${this::class}")
    }
}

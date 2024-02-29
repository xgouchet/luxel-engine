package fr.xgouchet.luxels.core.color

/**
 * A [ColorSource] with a single immutable color.
 * Boring.
 * @param color the color
 */
class StaticColorSource(
    private val color: Color,
) : ColorSource {

    // region ColorSource

    override fun color(): Color {
        return color
    }

    // endregion
}

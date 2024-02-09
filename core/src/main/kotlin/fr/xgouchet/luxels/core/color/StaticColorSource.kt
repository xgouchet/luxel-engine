package fr.xgouchet.luxels.core.color

class StaticColorSource(
    private val color: Color,
) : ColorSource {

    // region ColorSource

    override fun color(): Color {
        return color
    }

    // endregion
}

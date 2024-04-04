package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.render.exposure.Film

class StubFilm(resolution: Resolution) : Film {
    override val width: Int = resolution.width
    override val height: Int = resolution.height

    private val stubbedColor = mutableMapOf<Pair<Int, Int>, Color>()

    override fun expose(position: Vector2, color: Color) {
        TODO("Not yet implemented")
    }

    override fun getColor(i: Int, j: Int): Color {
        return stubbedColor[i to j] ?: Color.TRANSPARENT
    }

    fun stubColor(i: Int, j: Int, color: Color) {
        stubbedColor[i to j] = color
    }
}

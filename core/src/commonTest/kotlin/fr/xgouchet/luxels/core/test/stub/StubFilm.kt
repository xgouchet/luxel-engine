package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.render.exposure.Film

class StubFilm(resolution: Resolution) : Film {

    override val width: Int = resolution.width
    override val height: Int = resolution.height

    private val stubbedColor = mutableMapOf<Pair<Int, Int>, HDRColor>()

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        error("Not Implemented")
    }

    override fun getColor(i: Int, j: Int): HDRColor {
        return stubbedColor[i to j] ?: HDRColor.TRANSPARENT
    }

    fun stubColor(
        i: Int,
        j: Int,
        color: Color,
    ) {
        stubbedColor[i to j] = color.asHDR()
    }
}

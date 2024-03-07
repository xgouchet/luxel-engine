package fr.xgouchet.graphikio.data

import fr.xgouchet.graphikio.color.Color

interface RasterData {
    val width: Int

    val height: Int

    fun getColor(x: Int, y: Int): Color
}

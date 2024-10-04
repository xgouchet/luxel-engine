package fr.xgouchet.luxels.components.io

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.graphikio.data.MetadataAttribute
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.core.render.exposure.Film

internal class FilmRasterDataWrapper(
    val film: Film,
    iso: Double,
) : RasterData {
    private val scaledIso = iso / 1600.0

    init {
        check(!scaledIso.isNaN())
    }

    // region RasterData

    override val width: Int = film.width

    override val height: Int = film.height

    override fun getColor(x: Int, y: Int): Color {
        return film.getColor(x, y) * scaledIso
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return mapOf()
    }

    // endregion
}

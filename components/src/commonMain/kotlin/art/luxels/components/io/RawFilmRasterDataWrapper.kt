package art.luxels.components.io

import art.luxels.core.render.Film
import art.luxels.imageio.color.Color
import art.luxels.imageio.data.MetadataAttribute
import art.luxels.imageio.data.RasterData

internal class RawFilmRasterDataWrapper(val film: Film) : RasterData {
    // region RasterData

    override val width: Int = film.width

    override val height: Int = film.height

    override fun getColor(x: Int, y: Int): Color {
        return film.getColor(x, y)
    }

    override fun getMetadata(): Map<String, MetadataAttribute> {
        return mapOf()
    }

    // endregion
}

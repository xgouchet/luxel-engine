package fr.xgouchet.luxels.core.render

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A film that can be merged with other films.
 * @param resolution the resolution of the film
 */
class LayeredFilm(resolution: Resolution) : AbstractFilm(resolution) {
    private var layerCount = 0

    // region Exposure

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        error("Operation not supported")
    }

    // endregion

    // region Internal

    private val mergeMutex = Mutex()

    /**
     * Merges another film layer into this.
     * @param layer the film to merge
     */
    suspend fun mergeLayer(layer: Film) {
        // TODO add a blend mode ?
        require(layer.width == width && layer.height == height) {
            "Cannot merge layer with resolution ${layer.width}⨉${layer.height} in film with resolution $resolution"
        }

        if (layer.hasData()) {
            mergeMutex.withLock {
                for (i in 0..<width) {
                    for (j in 0..<height) {
                        expose(i, j, layer.getColor(i, j), 1.0)
                    }
                }
            }
        }

        layerCount++
    }

    // endregion
}

package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.graphikio.color.Color
import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class LayeredFilm(resolution: Resolution) : AbstractFilm(resolution) {
    private var layerCount = 0

    // region Film

    override fun expose(position: Vector<Dimension.D2>, color: Color) {
        error("Operation not supported")
    }

    // endregion

    // region Internal

    private val mergeMutex = Mutex()

    internal suspend fun mergeLayer(layer: Film) {
        require(layer.width == width && layer.height == height) {
            "Cannot merge layer with resolution ${layer.width}⨉${layer.height} in film with resolution $resolution"
        }

        mergeMutex.withLock {
            for (i in 0..<width) {
                for (j in 0..<height) {
                    expose(i, j, layer.getColor(i, j), 1.0)
                }
            }
        }

        layerCount++
    }

    // endregion
}

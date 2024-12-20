package fr.xgouchet.luxels.cli.series.pixie

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.color.asHDR
import fr.xgouchet.graphikio.data.OutOfBoundStrategy
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.data.SDRRasterData
import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import okio.Path
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.time.Duration

class PixieEnvironment(
    path: Path,
    simulationVolume: Volume<D2>,
) : BaseEnvironment<D2>(simulationVolume) {

    private val imageRasterData: RasterData = GraphikIO.read(path).apply {
        (this as? SDRRasterData)?.outOfBoundStrategy = OutOfBoundStrategy.LOOP
    }

    // region Environment

    override fun environmentColor(
        position: Vector<D2>,
        time: Duration,
    ): HDRColor {
        val uv = position / simulationVolume.size

        val x = floor(uv.x * imageRasterData.width).roundToInt()
        val y = floor(uv.y * imageRasterData.height).roundToInt()
        return imageRasterData.getColor(x, y).asHDR()
    }

    // endregion
}

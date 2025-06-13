package art.luxels.cli.series.s02pixie

import art.luxels.components.engine.BaseEnvironment
import art.luxels.core.math.Dimension.D2
import art.luxels.core.math.Vector
import art.luxels.core.math.Volume
import art.luxels.core.math.x
import art.luxels.core.math.y
import art.luxels.imageio.ImageIO
import art.luxels.imageio.color.HDRColor
import art.luxels.imageio.color.asHDR
import art.luxels.imageio.data.OutOfBoundStrategy
import art.luxels.imageio.data.RasterData
import art.luxels.imageio.data.SDRRasterData
import okio.Path
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.time.Duration

class PixieEnvironment(path: Path, simulationVolume: Volume<D2>) : BaseEnvironment<D2>(simulationVolume) {

    private val imageRasterData: RasterData = ImageIO.read(path).apply {
        (this as? SDRRasterData)?.outOfBoundStrategy = OutOfBoundStrategy.LOOP
    }

    // region Environment

    override fun environmentColor(position: Vector<D2>, time: Duration): HDRColor {
        val uv = position / simulationVolume.size

        val x = floor(uv.x * imageRasterData.width).roundToInt()
        val y = floor(uv.y * imageRasterData.height).roundToInt()
        return imageRasterData.getColor(x, y).asHDR()
    }

    // endregion
}

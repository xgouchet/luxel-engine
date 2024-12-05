package fr.xgouchet.luxels.cli.pixie

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.graphikio.data.SDRRasterData
import fr.xgouchet.luxels.components.render.projection.Flat2DProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.LogHandlerExt
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.math.x
import fr.xgouchet.luxels.core.math.y
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import okio.Path
import kotlin.time.Duration

internal class PixieSimulator : Simulator<Dimension.D2, PixieLuxel, Path> {

    private var imageRasterData: RasterData = SDRRasterData(1, 1)
    private var simSpace = Volume.unit(Dimension.D2)

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D2>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D2> {
        return Flat2DProjection(simulationSpace, filmSpace)
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D2>,
        inputData: InputData<Path>,
        logger: LogHandlerExt,
    ) {
        imageRasterData = GraphikIO.read(inputData.data)
        simSpace = simulation.volume
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D2>, time: Duration): PixieLuxel {
        val uv = Vector2(RndGen.double.uniform(), RndGen.double.uniform())
        val position = (uv * simulation.volume.size) + simulation.volume.min

        val (colorMask, iteration) = when (RndGen.int.uniform() % 3) {
            0 -> (HDRColor.RED * 0.05) to 20
            1 -> (HDRColor.GREEN * 0.01) to 100
            2 -> (HDRColor.BLUE * 0.002) to 500
            else -> HDRColor.TRANSPARENT to 1
        }

        return PixieLuxel(
            imageRasterData,
            position,
            uv,
            colorMask,
            iteration,
        )
    }

    override fun updateLuxel(luxel: PixieLuxel, time: Duration) {
        val color = luxel.colorSource.color()

        val homogenousPos = (luxel.position() - simSpace.center) / simSpace.size
        val homogenousDir = Vector2((color.hue() * 2.0) - 1.0, (color.saturation() * 2.0) - 1.0) * color.value()

        val newPosition = keepInRange(homogenousPos + (homogenousDir * SPEED_SCALE))

        luxel.colorSource.uv = ((newPosition + unitAxis) / 2.0)
        luxel.positionSource.position = (newPosition * simSpace.size) + simSpace.center
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D2>,
        time: Duration,
        animationDuration: Duration,
    ) {
        TODO("Not yet implemented")
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
        TODO("Not yet implemented")
    }

    // TODO Create render simulation worker exposing more
//    override fun exposeLuxel(luxel: PixieLuxel, filmExposition: (Vector<Dimension.D2>, Color) -> Unit) {
//        val basePosition = luxel.position() + RndGen.vector2.gaussian()
//        val baseColor = luxel.color()
//        for (i in 0..<5) {
//            val color = baseColor * (5.0 - i)
//            val offset = i * 3.0
//            filmExposition(basePosition + (xAxis * offset), color)
//            filmExposition(basePosition - (xAxis * offset), color)
//            filmExposition(basePosition + (yAxis * offset), color)
//            filmExposition(basePosition - (yAxis * offset), color)
//        }
//    }

    override fun outputName(): String {
        return "pixie"
    }

    // endregion

    // region Internal

    private fun keepInRange(value: Vector<Dimension.D2>): Vector<Dimension.D2> {
        return Vector2(
            keepInRange(value.x),
            keepInRange(value.y),
        )
    }

    private fun keepInRange(value: Double): Double {
        if (value < -1.0) {
            var v = value
            while (v < -1.0) v += 2.0
            return v
        }
        if (value > 1.0) {
            var v = value
            while (v > 1.0) v -= 2.0
            return v
        }
        return value
    }

    // endregion

    companion object {
        private const val SPEED_SCALE = 1.0

        val xAxis = Vector2(1.0, 0.0)
        val yAxis = Vector2(0.0, 1.0)
        val unitAxis = Vector2(1.0, 1.0)
    }
}

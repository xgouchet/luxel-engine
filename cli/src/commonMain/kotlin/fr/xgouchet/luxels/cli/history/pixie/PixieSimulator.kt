package fr.xgouchet.luxels.cli.history.pixie

import fr.xgouchet.graphikio.GraphikIO
import fr.xgouchet.graphikio.data.RasterData
import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.geometry.Space3
import fr.xgouchet.luxels.core.math.geometry.Vector2
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.simulation.Simulator
import okio.Path
import kotlin.time.Duration


internal class PixieSimulator : Simulator<PixieLuxel, Path> {

    private lateinit var imageRasterData: RasterData
    private var simSpace = Space3.UNIT

    override fun initEnvironment(simulation: Configuration.Simulation, inputData: InputData<Path>) {
        super.initEnvironment(simulation, inputData)
        imageRasterData = GraphikIO.read(inputData.data)
        simSpace = simulation.space
    }

    override fun spawnLuxel(simulation: Configuration.Simulation, time: Duration): PixieLuxel {

        val uv = Vector2(RndGen.double.uniform(), RndGen.double.uniform())
        val position = (Vector3(uv.x, uv.y, 0.0) * simulation.space.size) + simulation.space.min

        val (colorMask, iteration) = when (RndGen.int.uniform() % 3) {
            0 -> (Color.RED * 0.05) to 20
            1 -> (Color.GREEN * 0.01) to 100
            2 -> (Color.BLUE * 0.002) to 500
            else -> TODO()
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
        super.updateLuxel(luxel, time)

        val color = luxel.colorSource.color()

        val homogenousPos = (luxel.position() - simSpace.center) / simSpace.size
        val homogenousDir = Vector3(
            (color.hue() * 2.0) - 1.0,
            (color.saturation() * 2.0) - 1.0,
            0.0
        ) * color.value()

        val newPosition = keepInRange(homogenousPos + (homogenousDir * SPEED_SCALE))

        luxel.colorSource.uv = ((newPosition + Vector3.UNIT) / 2.0).xy
        luxel.positionSource.position = (newPosition * simSpace.size) + simSpace.center
    }

    override fun exposeLuxel(luxel: PixieLuxel, filmExposition: (Vector3, Color) -> Unit) {
        val basePosition = luxel.position() + (RndGen.vector3.gaussian())
        val baseColor = luxel.color()
        for (i in 0..<5) {
            val color = baseColor * (5.0 - i)
            val offset = i * 3.0
            filmExposition(basePosition + (Vector3.X_AXIS * offset), color)
            filmExposition(basePosition - (Vector3.X_AXIS * offset), color)
            filmExposition(basePosition + (Vector3.Y_AXIS * offset), color)
            filmExposition(basePosition - (Vector3.Y_AXIS * offset), color)
            filmExposition(basePosition + (Vector3.Z_AXIS * offset), color)
            filmExposition(basePosition - (Vector3.Z_AXIS * offset), color)
        }
    }

    override fun outputName(): String {
        return "pixie"
    }

    private fun keepInRange(value: Vector3): Vector3 {
        return Vector3(
            keepInRange(value.x),
            keepInRange(value.y),
            keepInRange(value.z),
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

    companion object {
        private const val SPEED_SCALE = 1.0
    }
}
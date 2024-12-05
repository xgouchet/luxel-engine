package fr.xgouchet.luxels.cli.demo.projection

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.cli.demo.DemoLuxel
import fr.xgouchet.luxels.components.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.log.LogHandler
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.math.random.RndGen
import fr.xgouchet.luxels.core.render.projection.Projection
import fr.xgouchet.luxels.core.simulation.Simulator
import kotlin.time.Duration

internal class CubeSimulator : Simulator<Dimension.D3, DemoLuxel<Dimension.D3>, Unit> {

    // region Simulator

    override fun getProjection(
        simulationSpace: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        return PerspectiveProjection(
            simulationSpace,
            filmSpace,
            simulationSpace.center + (Vector3(1.0, 0.2, 0.5) * simulationSpace.size.length()),
            simulationSpace.center,
        )
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Unit>,
        logger: LogHandler,
    ) {
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
    }

    @Suppress("CyclomaticComplexMethod")
    override suspend fun spawnLuxel(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
    ): DemoLuxel<Dimension.D3> {
        val t = RndGen.double.inRange(-1.0, 1.0)
        val edge = RndGen.int.uniform() % 12

        val p = when (edge) {
            0 -> Vector3(t, -1.0, -1.0)
            1 -> Vector3(t, -1.0, 1.0)
            2 -> Vector3(t, 1.0, -1.0)
            3 -> Vector3(t, 1.0, 1.0)

            4 -> Vector3(-1.0, t, -1.0)
            5 -> Vector3(-1.0, t, 1.0)
            6 -> Vector3(1.0, t, -1.0)
            7 -> Vector3(1.0, t, 1.0)

            8 -> Vector3(-1.0, -1.0, t)
            9 -> Vector3(-1.0, 1.0, t)
            10 -> Vector3(1.0, -1.0, t)
            11 -> Vector3(1.0, 1.0, t)

            else -> Vector.nul(Dimension.D3)
        }

        val c = t + 1.0
        val color = when (edge) {
            0 -> HDRColor(c, 0.0, 0.0)
            1 -> HDRColor(c, 0.0, 1.0)
            2 -> HDRColor(c, 1.0, 0.0)
            3 -> HDRColor(c, 1.0, 1.0)

            4 -> HDRColor(0.0, c, 0.0)
            5 -> HDRColor(0.0, c, 1.0)
            6 -> HDRColor(1.0, c, 0.0)
            7 -> HDRColor(1.0, c, 1.0)

            8 -> HDRColor(0.0, 0.0, c)
            9 -> HDRColor(0.0, 1.0, c)
            10 -> HDRColor(1.0, 0.0, c)
            11 -> HDRColor(1.0, 1.0, c)

            else -> HDRColor.BLACK
        }

        val position = (p * simulation.volume.size * 0.1) + simulation.volume.center

        return DemoLuxel(position, color)
    }

    override fun updateLuxel(luxel: DemoLuxel<Dimension.D3>, time: Duration) {
    }

    override fun outputName(): String {
        return "cube"
    }

    // endregion
}

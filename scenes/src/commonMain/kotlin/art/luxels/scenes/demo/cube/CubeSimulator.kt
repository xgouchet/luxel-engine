package art.luxels.scenes.demo.cube

import art.luxels.components.engine.BaseEnvironment
import art.luxels.components.engine.StaticLuxel
import art.luxels.core.math.Dimension.D3
import art.luxels.core.math.Vector
import art.luxels.core.math.Vector3
import art.luxels.core.math.random.RndGen
import art.luxels.engine.api.Simulator
import art.luxels.engine.simulation.runner.FrameInfo
import art.luxels.imageio.color.HDRColor
import kotlin.math.PI
import kotlin.math.cos

class CubeSimulator : Simulator<D3, StaticLuxel<D3>, BaseEnvironment<D3>> {

    // region Simulator

    @Suppress("CyclomaticComplexMethod")
    override fun spawnLuxel(environment: BaseEnvironment<D3>, frameInfo: FrameInfo): StaticLuxel<D3> {
        val a = (frameInfo.time.inWholeMilliseconds / 1000.0) * PI
        val volumeSize = environment.simulationVolume.size
        val cubeCenter = when (RndGen.int.inRange(0, 65536) % 7) {
            0 -> Vector3(0.5, cos(a + 0.3) * 0.1, 0.0) * volumeSize.length()
            1 -> Vector3(0.25, cos(a + 0.6) * 0.1, 0.0) * volumeSize.length()
            2 -> Vector3(0.0, cos(a + 0.9) * 0.1, 0.0) * volumeSize.length()
            3 -> Vector3(-0.25, cos(a + 1.2) * 0.1, 0.0) * volumeSize.length()
            4 -> Vector3(-0.5, cos(a + 1.5) * 0.1, 0.0) * volumeSize.length()
            5 -> Vector3(0.0, 0.0, 0.3) * volumeSize.length()
            6 -> Vector3(0.0, 0.0, -0.3) * volumeSize.length()
            else -> TODO()
        }

        val t = RndGen.double.inRange(-1.0, 1.0)
        val edge = RndGen.int.inRange(0, 65536) % 12

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

            else -> Vector.nul(D3)
        }

        val c = t + 1.0
        when (edge) {
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

        val position = (p * volumeSize * 0.1) + cubeCenter + environment.simulationVolume.center

        return StaticLuxel(position, HDRColor.WHITE)
    }

    override fun updateLuxel(
        luxel: StaticLuxel<D3>,
        environment: BaseEnvironment<D3>,
        frameInfo: FrameInfo,
    ) {
    }

    // endregion
}

package fr.xgouchet.luxels.cli.demo.cube

import fr.xgouchet.luxels.components.engine.BaseEnvironment
import fr.xgouchet.luxels.components.engine.StaticLuxel
import fr.xgouchet.luxels.components.render.projection.PerChannelProjection
import fr.xgouchet.luxels.components.render.projection.PerspectiveProjection
import fr.xgouchet.luxels.core.math.Dimension.D2
import fr.xgouchet.luxels.core.math.Dimension.D3
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.engine.api.Scene
import fr.xgouchet.luxels.engine.api.Simulator
import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.render.Projection
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import kotlin.time.Duration

class CubeScene : Scene<D3, StaticLuxel<D3>, Unit, BaseEnvironment<D3>> {

    lateinit var environment: BaseEnvironment<D3>

    // region Scene

    override fun prepareScene(
        simulationVolume: Volume<D3>,
        duration: Duration,
        inputData: InputData<Unit>,
    ) {
        environment = BaseEnvironment(simulationVolume)
    }

    override fun getFrameEnvironment(frameInfo: FrameInfo): BaseEnvironment<D3> {
        return environment
    }

    override fun getProjection(
        simulationVolume: Volume<D3>,
        filmSpace: Volume<D2>,
        frameInfo: FrameInfo,
    ): Projection<D3> {
        val cameraOffset = Vector3(1.0, -0.2, 0.3) * simulationVolume.size.length()
        val red = PerspectiveProjection(
            simulationVolume = simulationVolume,
            filmSpace = filmSpace,
            cameraPosition = simulationVolume.center + cameraOffset,
            targetPosition = simulationVolume.center,
            settings = PerspectiveProjection.Settings(
                fov = 90.0,
                dofFocusDistance = (frameInfo.index * 25.0) + 2000.0,
                dofStrength = 10.0,
                dofSamples = 192,
            ),
        )
        val green = PerspectiveProjection(
            simulationVolume = simulationVolume,
            filmSpace = filmSpace,
            cameraPosition = simulationVolume.center + cameraOffset,
            targetPosition = simulationVolume.center,
            settings = PerspectiveProjection.Settings(
                fov = 91.0,
                dofFocusDistance = (frameInfo.index * 25.0) + 2000.0,
                dofStrength = 7.5,
                dofSamples = 128,
            ),
        )
        val blue = PerspectiveProjection(
            simulationVolume = simulationVolume,
            filmSpace = filmSpace,
            cameraPosition = simulationVolume.center + cameraOffset,
            targetPosition = simulationVolume.center,
            settings = PerspectiveProjection.Settings(
                fov = 92.0,
                dofFocusDistance = (frameInfo.index * 25.0) + 2000.0,
                dofStrength = 5.0,
                dofSamples = 96,
            ),
        )
        return PerChannelProjection(red, green, blue)
    }

    override fun initSimulator(): Simulator<D3, StaticLuxel<D3>, BaseEnvironment<D3>> {
        return CubeSimulator()
    }

    override fun outputName(): String {
        return "cube"
    }

    // endregion
}

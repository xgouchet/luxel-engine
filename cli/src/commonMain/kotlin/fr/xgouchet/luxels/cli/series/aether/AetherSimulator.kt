package fr.xgouchet.luxels.cli.series.aether

/*
class AetherSimulator(
    private val luxelLifespan: Int = 0x1000,
) : Simulator<Dimension.D3, AetherLuxel, Long> {

    private var gaussianRange = 0
    private var curves: List<Curve<Dimension.D3>> = emptyList()
    private var successiveStep = 0.0001
    private var frameCenterP: Double = 0.0

    // region Simulator

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Long>,
        logger: LogHandler,
    ) {
        gaussianRange = (simulation.quality.count shr 3).toInt()
        successiveStep = 0.1 / (simulation.quality.count shr 2)

        val curveCount = RndGen.int.inRange(5, 8)
        val pointCount = RndGen.int.inRange(4, 8)
        curves = List(curveCount) {
            Curve(List(pointCount) { RndGen.vector3.inVolume(simulation.volume) })
        }
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
        frameCenterP = if (animationDuration > 16.milliseconds) {
            (time / animationDuration)
        } else {
            RndGen.double.inRange(0.25, 0.75)
        }
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): AetherLuxel {
        val offsetP = RndGen.int.gaussian(0, gaussianRange) * successiveStep
        val p = frameCenterP + offsetP

        val t = (p * WL_RANGE) + WL_MIN
        val curve = Curve(curves.map { it.getPosition(p) })
        return AetherLuxel(curve, t, luxelLifespan)
    }

    override fun updateLuxel(luxel: AetherLuxel, time: Duration) {
    }

    override fun getProjection(
        simulationVolume: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        val angle = (time.inWholeMilliseconds / 1000.0) * (PI / 2.0)
        val offset = fromSpherical(angle, 0.0, simulationVolume.size.length())
        return PerspectiveProjection(
            simulationVolume,
            filmSpace,
            simulationVolume.center + offset,
            simulationVolume.center,
            fov = 70.0,
        )
    }

    override fun outputName(): String {
        return "aether"
    }

    // endregion

    companion object {
        private const val WL_MIN = EMSColorSource.MIN_UV_LIGHT
        private const val WL_RANGE = EMSColorSource.MAX_IR_LIGHT - EMSColorSource.MIN_UV_LIGHT
    }
}
 */

package fr.xgouchet.luxels.cli.wip.bookworm

/*
class BookwormSimulator(
    val luxelLifespan: Int = 0X80,
    val interpolation: Interpolation = Interpolation.Linear,
) : Simulator<Dimension.D3, BookwormLuxel, Path> {

    val inputReader = BookwormInputReader()

    val rng = VectorRandomGenerator(Dimension.D3)

    @Suppress("LateinitUsage")
    lateinit var input: BookwormInput

    @Suppress("LateinitUsage")
    lateinit var logger: LogHandler

    // region Simulator

    override fun outputName(): String {
        return "bookworm"
    }

    override fun initEnvironment(
        simulation: Configuration.Simulation<Dimension.D3>,
        inputData: InputData<Path>,
        logger: LogHandler,
    ) {
        this.input = inputReader.getInput(inputData.data, logger)
        this.logger = logger
    }

    override suspend fun spawnLuxel(simulation: Configuration.Simulation<Dimension.D3>, time: Duration): BookwormLuxel {
        val sentence = input.nextSentence()
        val wavelengthProgress = sentence.sentenceIndex.toDouble() / input.getSentenceCount()

        return BookwormLuxel(
            rng,
            sentence,
            interpolation.interpolate(
                EMSColorSource.MIN_VISIBLE_LIGHT,
                EMSColorSource.MAX_VISIBLE_LIGHT,
                wavelengthProgress,
            ),
            luxelLifespan,
        )
    }

    override fun updateLuxel(luxel: BookwormLuxel, time: Duration) {
    }

    override fun getProjection(
        simulationVolume: Volume<Dimension.D3>,
        filmSpace: Volume<Dimension.D2>,
        time: Duration,
    ): Projection<Dimension.D3> {
        // For now a 3D projection rotation around the Y axis, 1 full rotation / sec
        // TODO something better ?
        val angle = (time.inWholeMilliseconds / 1000.0) * TAU
        val offset = fromSpherical(angle + HALF_PI, 0.0, simulationVolume.size.length())
        return PerspectiveProjection(
            simulationVolume,
            filmSpace,
            simulationVolume.center + offset,
            simulationVolume.center,
            fov = 70.0,
        )
    }

    override fun onFrameStart(
        simulation: Configuration.Simulation<Dimension.D3>,
        time: Duration,
        animationDuration: Duration,
    ) {
    }

    override fun onFrameEnd(time: Duration, animationDuration: Duration) {
    }

    // endregion
}
 */

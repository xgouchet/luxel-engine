package fr.xgouchet.luxels.engine.test.kotest.property

import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.configuration.Configuration
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.Quality
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.InternalConfiguration
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import fr.xgouchet.luxels.engine.test.stub.StubInputSource
import fr.xgouchet.luxels.engine.test.stub.stubInputData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long

fun configurationArb() = arbitrary {
    val dimension = dimensionArb().bind()
    Configuration<Dimension, Long>(
        dimension = dimension,
        input = Configuration.Input<Long>(
            source = StubInputSource(Arb.list(Arb.long()).bind()),
        ),
        simulation = Configuration.Simulation(
            volume = volumeArb(dimension).bind(),
            quality = Arb.enum<Quality>().bind(),
            maxThreadCount = Arb.int(1, 128).bind(),
            simulationType = Arb.enum<SimulationType>().bind(),
        ),
        render = Configuration.Render(
            filmType = Arb.enum<FilmType>().bind(),
            resolution = Arb.enum<Resolution>().bind(),
            fixer = NoOpFixer(), // TODO
        ),
        animation = Configuration.Animation(
            duration = shortDurationArb().bind(),
            fps = Arb.int(1, 120).bind(),
        ),
    )
}

internal fun internalConfigurationArb() = arbitrary {
    val frameStep = shortDurationArb().bind()
    val frameCount = Arb.int(0, 128).bind()
    val frameIdx = Arb.int(0, frameCount).bind()
    val dimension = dimensionArb().bind()
    InternalConfiguration<Dimension, Long>(
        dimension = dimension,
        inputData = stubInputData(Arb.int().bind(), Arb.long().bind()),
        simulationVolume = volumeArb(dimension).bind(),
        simulationLuxelCount = Arb.long(min = 1).bind(),
        simulationMaxThreadCount = Arb.int(1, 128).bind(),
        simulationType = Arb.enum<SimulationType>().bind(),
        animationDuration = (frameStep * frameCount),
        animationFrameStep = frameStep,
        animationFrameInfo = FrameInfo(frameIdx, frameStep * frameIdx),
        outputFilmType = Arb.enum<FilmType>().bind(),
        outputResolution = Arb.enum<Resolution>().bind(),
        outputFixer = NoOpFixer(), // TODO
    )
}

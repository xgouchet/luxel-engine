package fr.xgouchet.luxels.engine.test.kotest.property

import dev.mokkery.mock
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.Environment
import fr.xgouchet.luxels.engine.api.configuration.FilmType
import fr.xgouchet.luxels.engine.api.configuration.SimulationType
import fr.xgouchet.luxels.engine.simulation.CommonConfiguration
import fr.xgouchet.luxels.engine.simulation.SceneConfiguration
import fr.xgouchet.luxels.engine.simulation.SimulationContext
import fr.xgouchet.luxels.engine.simulation.runner.FrameInfo
import fr.xgouchet.luxels.engine.test.stub.StubInputSource
import fr.xgouchet.luxels.engine.test.stub.stubInputData
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long

fun inputSourceArb() = arbitrary {
    StubInputSource(Arb.list(Arb.long()).bind())
}

internal fun sceneConfigurationArb() = arbitrary {
    val dimension = dimensionArb().bind()
    SceneConfiguration<Dimension, Long, Environment<Dimension>>(
        dimension = dimension,
        inputData = stubInputData(Arb.int().bind(), Arb.long().bind()),
        simulationVolume = volumeArb(dimension).bind(),
        context = SimulationContext(mock(), mock()),
    )
}

internal fun commonConfigurationArb() = arbitrary {
    val frameStep = shortDurationArb().bind()
    val frameCount = Arb.int(0, 128).bind()
    val frameIdx = Arb.int(0, frameCount).bind()
    val progression = frameIdx.toDouble() / frameCount
    CommonConfiguration(
        simulationLuxelCount = Arb.long(min = 1).bind(),
        simulationMaxThreadCount = Arb.int(1, 128).bind(),
        simulationType = Arb.enum<SimulationType>().bind(),
        animationDuration = (frameStep * frameCount),
        animationFrameStep = frameStep,
        animationFrameInfo = FrameInfo(frameIdx, frameStep * frameIdx, progression),
        outputFilmType = Arb.enum<FilmType>().bind(),
        outputResolution = Arb.enum<Resolution>().bind(),
        outputFixer = mock(),
    )
}

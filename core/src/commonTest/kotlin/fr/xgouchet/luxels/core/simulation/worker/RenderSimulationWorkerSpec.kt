package fr.xgouchet.luxels.core.simulation.worker

import fr.xgouchet.luxels.core.configuration.Resolution
import fr.xgouchet.luxels.core.configuration.configuration
import fr.xgouchet.luxels.core.log.Logger
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.test.kotest.property.colorArb
import fr.xgouchet.luxels.core.test.kotest.property.durationArb
import fr.xgouchet.luxels.core.test.kotest.property.vectorArb
import fr.xgouchet.luxels.core.test.stub.StubFilm
import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import fr.xgouchet.luxels.core.test.stub.StubLuxel
import fr.xgouchet.luxels.core.test.stub.StubProjection
import fr.xgouchet.luxels.core.test.stub.StubSimulator
import fr.xgouchet.luxels.core.test.stub.core.stubResponse
import fr.xgouchet.luxels.core.test.stub.core.verifyCall
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum

class RenderSimulationWorkerSpec : DescribeSpec(
    {
        describe("simulateSingleLuxel [3D]") {
            it("spawns a luxel") {
                checkAll(
                    Exhaustive.enum<Resolution>(),
                    durationArb(),
                    Arb.long(1024L, 65536L),
                    Arb.long(0L, 65536L),
                    vectorArb(Dimension.D3),
                    colorArb(),
                    vectorArb(Dimension.D2),
                ) { resolution, frameTime, luxelCount, luxelIndex, luxelPos, luxelColor, filmPos ->
                    val stubLuxel = StubLuxel<Dimension.D3>()
                    stubLuxel.stubResponse("position").withValue(luxelPos)
                    stubLuxel.stubResponse("color").withValue(luxelColor)
                    stubLuxel.stubResponse("isAlive").withValues(true, true, true, true, false)
                    val stubSimulator = StubSimulator<Dimension.D3, Unit>()
                    stubSimulator.stubResponse("spawnLuxel").withValue(stubLuxel)
                    val stubFilm = StubFilm(resolution)
                    val stubProjection = StubProjection<Dimension.D3>()
                    stubProjection.stubResponse("convertPosition", mapOf("position" to luxelPos)).withValue(filmPos)
                    val logger = Logger(StubLogHandler())
                    val configuration = configuration(Dimension.D3) {}
                    val worker = RenderSimulationWorker(
                        stubFilm,
                        stubSimulator,
                        configuration.simulation,
                        stubProjection,
                        frameTime,
                        luxelCount,
                        logger,
                    )

                    worker.simulateSingleLuxel(luxelIndex)

                    stubSimulator.verifyCall(
                        name = "spawnLuxel",
                        params = mapOf("simulation" to configuration.simulation, "time" to frameTime),
                    )
                    stubFilm.verifyCall(
                        name = "expose",
                        params = mapOf("position" to filmPos, "color" to luxelColor),
                        times = 5,
                    )
                }
            }
        }
    },
)

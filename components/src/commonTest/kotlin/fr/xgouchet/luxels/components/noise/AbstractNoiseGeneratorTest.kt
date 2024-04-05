package fr.xgouchet.luxels.components.noise

import fr.xgouchet.luxels.components.test.kotest.property.doubleArb
import fr.xgouchet.luxels.core.math.EPSILON
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.describeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlin.math.abs

fun abstractNoiseGeneratorTest(description: String, noiseProvider: () -> DimensionalNoiseGenerator) = describeSpec {
    describe("generic noise [$description]") {
        it("returns a value with the expected dimensions") {
            checkAll(Arb.list(doubleArb(), 1..8), Arb.int(1, 8)) { input, outputSize ->
                val noise = noiseProvider()
                val result = noise.noise(input, outputSize)

                result shouldHaveSize outputSize
            }
        }

        it("always returns a value between 0.0 and 1.0") {
            checkAll(Arb.list(doubleArb(), 1..8), Arb.int(1, 8)) { input, outputSize ->
                val noise = noiseProvider()
                val result = noise.noise(input, outputSize)

                assertSoftly {
                    result.forEach {
                        it shouldBeGreaterThanOrEqual 0.0
                        it shouldBeLessThanOrEqual 1.0
                    }
                }
            }
        }

        it("is continuous") {
            checkAll(Arb.list(doubleArb(), 1..8), Arb.int(1, 8), Arb.int(1, 8)) { input, outputSize, dimension ->
                val noise = noiseProvider()
                val changingIndex = dimension % input.size
                val input2 = input.mapIndexed { index, d ->
                    if (index == changingIndex) {
                        d + (EPSILON / 10.0)
                    } else {
                        d
                    }
                }

                val result1 = noise.noise(input, outputSize)
                val result2 = noise.noise(input2, outputSize)

                assertSoftly {
                    result1.indices.forEach { i ->
                        val diff = abs(result2[i] - result1[i])
                        diff shouldBeLessThanOrEqual (EPSILON * 10.0)
                    }
                }
            }
        }
    }
}

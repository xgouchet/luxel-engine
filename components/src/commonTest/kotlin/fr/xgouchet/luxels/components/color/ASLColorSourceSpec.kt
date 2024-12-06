package fr.xgouchet.luxels.components.color

import fr.xgouchet.luxels.components.color.EMSColorSource.Companion.MAX_IR_LIGHT
import fr.xgouchet.luxels.components.color.EMSColorSource.Companion.MIN_UV_LIGHT
import fr.xgouchet.luxels.components.color.atomic.ASLColorSource
import fr.xgouchet.luxels.components.color.atomic.PeriodicTable
import fr.xgouchet.luxels.components.test.kotest.assertions.shouldBeCloseTo
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.doubles.shouldBeLessThanOrEqual
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class ASLColorSourceSpec : DescribeSpec(
    {
        describe("spectralLines") {
            PeriodicTable.allElements.forEach { element ->
                it("${element.name} only has visible wavelengths") {
                    element.spectralLines.forEach { line ->
                        line.waveLength shouldBeGreaterThanOrEqual MIN_UV_LIGHT
                        line.waveLength shouldBeLessThanOrEqual MAX_IR_LIGHT
                    }
                }
            }
        }

        describe("color") {
            it("computes color from spectral lines ") {
                checkAll(
                    Arb.string(),
                    Arb.string(),
                    Arb.int(),
                    Arb.double(MIN_UV_LIGHT, MAX_IR_LIGHT),
                    Arb.double(min = 0.0, max = 100.0),
                ) { name, symbol, number, wavelength, intensity ->
                    val colorSource = ASLColorSource(
                        name,
                        symbol,
                        number,
                        listOf(ASLColorSource.SpectralLine(wavelength, intensity)),
                    )
                    val reference = EMSColorSource(wavelength, intensity)

                    val color = colorSource.color()
                    val refColor = reference.color()

                    color shouldBeCloseTo refColor
                }
            }
        }
    },
)

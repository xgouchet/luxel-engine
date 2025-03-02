package art.luxels.components.color.atomic

import art.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Lithium.
 */
object Lithium : ASLColorSource(
    "Lithium",
    "Li",
    3,
    listOf(
        SpectralLine(391.530, 0.02),
        SpectralLine(391.535, 0.02),
        SpectralLine(391.535, 0.02),
        SpectralLine(398.548, 0.01),
        SpectralLine(398.554, 0.01),
        SpectralLine(413.256, 0.04),
        SpectralLine(413.262, 0.04),
        SpectralLine(413.262, 0.04),
        SpectralLine(427.307, 0.02),
        SpectralLine(427.313, 0.02),
        SpectralLine(460.283, 0.01),
        SpectralLine(460.289, 0.01),
        SpectralLine(460.289, 0.01),
        SpectralLine(548.355, 0.60),
        SpectralLine(548.565, 0.60),
        SpectralLine(610.354, 0.32),
        SpectralLine(610.365, 0.32),
        SpectralLine(670.776, 3.60),
        SpectralLine(670.791, 3.60),
    ),
)

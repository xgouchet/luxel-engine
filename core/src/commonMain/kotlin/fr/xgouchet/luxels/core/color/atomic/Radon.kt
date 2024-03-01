package fr.xgouchet.luxels.core.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Radon.
 */
object Radon : ASLColorSource(
    "Radon",
    "Rn",
    86,
    listOf(
        SpectralLine(434.960, 0.10),
        SpectralLine(705.542, 0.20),
        SpectralLine(726.811, 0.10),
        SpectralLine(745.000, 0.30),
    ),
)

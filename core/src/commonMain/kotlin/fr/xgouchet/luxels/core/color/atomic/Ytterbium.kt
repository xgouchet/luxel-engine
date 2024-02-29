package fr.xgouchet.luxels.core.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Ytterbium.
 */
object Ytterbium: ASLColorSource(
    "Ytterbium",
    "Yb",
    70,
    listOf(
        SpectralLine(369.419, 32.00),
        SpectralLine(398.799, 32.00),
        SpectralLine(399.088, 0.93),
        SpectralLine(457.621, 0.64),
        SpectralLine(493.550, 0.71),
        SpectralLine(555.647, 2.40),
        SpectralLine(679.960, 0.69),
        SpectralLine(769.948, 0.75),
    )
)

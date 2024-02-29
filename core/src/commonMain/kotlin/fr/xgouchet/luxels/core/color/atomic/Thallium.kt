package fr.xgouchet.luxels.core.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Thallium.
 */
object Thallium : ASLColorSource(
    "Thallium",
    "Tl",
    81,
    listOf(
        SpectralLine(377.572, 12.00),
        SpectralLine(535.046, 18.00),
    )
)

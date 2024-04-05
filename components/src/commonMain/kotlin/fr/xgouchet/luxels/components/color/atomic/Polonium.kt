package fr.xgouchet.luxels.components.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Polonium.
 */
object Polonium : ASLColorSource(
    "Polonium",
    "Po",
    84,
    listOf(
        SpectralLine(417.052, 1.20),
    ),
)

package fr.xgouchet.luxels.components.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Arsenic.
 */
object Arsenic : ASLColorSource(
    "Arsenic",
    "As",
    33,
    listOf(
        SpectralLine(506.898, 0.10),
        SpectralLine(512.134, 0.10),
        SpectralLine(514.163, 0.10),
        SpectralLine(514.163, 0.10),
        SpectralLine(540.813, 0.10),
        SpectralLine(545.132, 0.14),
    ),
)

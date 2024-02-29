package fr.xgouchet.luxels.core.color.atomic

import fr.xgouchet.luxels.core.color.ColorSource

/**
 * The Atomic Spectral Line [ColorSource] for Hydrogen.
 */
object Hydrogen: ASLColorSource(
    "Hydrogen",
    "H",
    1,
    listOf(
        SpectralLine(365.665, 0.70),
        SpectralLine(365.725, 0.70),
        SpectralLine(365.804, 0.70),
        SpectralLine(365.865, 0.70),
        SpectralLine(365.941, 0.70),
        SpectralLine(366.032, 0.70),
        SpectralLine(366.127, 0.80),
        SpectralLine(366.222, 1.10),
        SpectralLine(366.341, 0.90),
        SpectralLine(366.465, 1.00),
        SpectralLine(366.608, 0.90),
        SpectralLine(366.773, 1.00),
        SpectralLine(366.945, 1.30),
        SpectralLine(367.132, 1.20),
        SpectralLine(367.381, 1.30),
        SpectralLine(367.6376, 1.40),
        SpectralLine(367.9370, 1.70),
        SpectralLine(368.2823, 1.70),
        SpectralLine(368.6831, 2.00),
        SpectralLine(369.1551, 2.30),
        SpectralLine(369.7157, 2.30),
        SpectralLine(370.3859, 2.80),
        SpectralLine(371.1978, 3.30),
        SpectralLine(372.1946, 5.00),
        SpectralLine(373.4369, 6.00),
        SpectralLine(375.0151, 8.00),
        SpectralLine(377.0633, 9.00),
        SpectralLine(379.7909, 17.00),
        SpectralLine(383.5397, 30.00),
        SpectralLine(388.9064, 70.00),
        SpectralLine(397.0075, 30.00),
        SpectralLine(410.1734, 70.00),
        SpectralLine(434.0472, 90.00),
        SpectralLine(486.135, 180.00),
        SpectralLine(656.279, 500.00),
    )
)

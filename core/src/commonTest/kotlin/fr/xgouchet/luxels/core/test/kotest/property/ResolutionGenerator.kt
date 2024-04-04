package fr.xgouchet.luxels.core.test.kotest.property

import fr.xgouchet.luxels.core.configuration.Resolution
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.of

fun resolutionArb() = arbitrary {
    Arb.of(
        Resolution.SQUARE_720,
        Resolution.SQUARE_1080,
        Resolution.SQUARE_1440,
        Resolution.SQUARE_2160, // OOM
        Resolution.SQUARE_2880, // OOM
        // Resolution.SQUARE_4320, // OOM
        // 4/3 resolutions
        Resolution.QVGA,
        Resolution.HVGA,
        Resolution.VGA,
        Resolution.PAL,
        Resolution.XGA,
        Resolution.UXGA,
        Resolution.QXGA,
        // 16/9 resolutions
        Resolution.HD_720,
        Resolution.FHD_1080,
        Resolution.QHD,
        Resolution.UHD_4K,
        // Resolution.UHD_5K, // OOM
        // Resolution.UHD_8K, // OOM
    ).bind()
}

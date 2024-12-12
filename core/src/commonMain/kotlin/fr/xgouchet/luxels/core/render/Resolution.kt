package fr.xgouchet.luxels.core.render

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector2

/**
 * The resolution of an image.
 * @property width the width of the film (in pixels)
 * @property height the height of the film (in pixels)
 */
enum class Resolution(val width: Int, val height: Int) {
    /** A square of 128×128 (16'384) pixels, mostly for smaller test runs in CI. */
    SQUARE_128(128, 128),

    /** A square of 720×720 (518'400) pixels. */
    SQUARE_720(720, 720),

    /** A square of 1080×1080 (1'166'400) pixels. */
    SQUARE_1080(1080, 1080),

    /** A square of 1440×1440 (2'073'600) pixels. */
    SQUARE_1440(1440, 1440),

    /** A square of 2160×2160 (4'665'600) pixels. */
    SQUARE_2160(2160, 2160),

    /** A square of 2880×2880 (8'294'400) pixels. */
    SQUARE_2880(2880, 2880),

    /** A square of 4320×4320 (18'662'400) pixels. */
    SQUARE_4320(4320, 4320),

    /** Quarter VGA: 320×240 (76'800) pixels. */
    QVGA(320, 240),

    /** Half VGA: 480×320 (153'600) pixels. */
    HVGA(480, 320),

    /** VGA: 640×480 (307'200) pixels. */
    VGA(640, 480),

    /** PAL: 768×576 (442'368) pixels. */
    PAL(768, 576),

    /** XGA: 1024×768 (786'432) pixels. */
    XGA(1024, 768),

    /** Ultra-XGA: 1600×1200 (1'920'000) pixels. */
    UXGA(1600, 1200),

    /** Quad-XGA: 2048×1536 (3'145'728) pixels. */
    QXGA(2048, 1536),

    /** HD 720p: 1280×720 (921'600) pixels. */
    HD_720(1280, 720),

    /** Full HD 1080p: 1920×1080 (2'073'600) pixels. */
    FHD_1080(1920, 1080),

    /** Quad HD: 2560×1440 (3'686'400) pixels. */
    QHD(2560, 1440),

    /** Ultra HD: 3840×2160 (8'294'400) pixels. */
    UHD_4K(3840, 2160),

    /** Ultra HD: 5120×2880 (14'745'600) pixels. */
    UHD_5K(5120, 2880),

    /** Ultra HD: 7680×4320 (33'177'600) pixels. */
    UHD_8K(7680, 4320),
    ;

    /** The number of pixels in the final image. */
    val pixelCount: Int = width * height

    /**
     * @return the resolution as a [Vector]
     */
    fun asVector2(): Vector<Dimension.D2> {
        return Vector2(width.toDouble(), height.toDouble())
    }

    // region Any

    override fun toString(): String {
        return "$name ($width⨉$height)"
    }

    // endregion
}

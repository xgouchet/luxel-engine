package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Space2
import fr.xgouchet.luxels.core.position.Space3
import kotlin.math.max

/**
 * The resolution of the film capturing the simulation rendering.
 * @property width the width of the film (in pixels)
 * @property height the height of the film (in pixels)
 */
enum class Resolution(val width: Int, val height: Int) {
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

    /**
     * @return the resolution as a [Vector2]
     */
    fun asVector2(): Vector2 {
        return Vector2(width.toDouble(), height.toDouble())
    }

    /**
     * @return the resolution as a [Space2]
     */
    fun asSpace2(): Space2 {
        return Space2(Vector2.NULL, asVector2())
    }

    /**
     * @return the resolution as a [Vector3]
     */
    fun asVector3(): Vector3 {
        return Vector3(width.toDouble(), height.toDouble(), max(width, height).toDouble())
    }

    /**
     * @return the resolution as a [Space3]
     */
    fun asSpace3(): Space3 {
        return Space3(Vector3.NULL, asVector3())
    }

    // region Any

    override fun toString(): String {
        return "$name ($width⨉$height)"
    }

    // endregion
}

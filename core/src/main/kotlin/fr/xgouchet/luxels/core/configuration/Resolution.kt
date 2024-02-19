package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import fr.xgouchet.luxels.core.position.Box
import fr.xgouchet.luxels.core.position.Rectangle

enum class Resolution(val width: Int, val height: Int) {
    // Square resolutions
    SQUARE_720(720, 720),
    SQUARE_1080(1080, 1080),
    SQUARE_1440(1440, 1440),
    SQUARE_2160(2160, 2160),
    SQUARE_2880(2880, 2880),
    SQUARE_4320(4320, 4320),

    // 4/3 resolutions
    QVGA(320, 240),
    HVGA(480, 320),
    VGA(640, 480),
    PAL(768, 576),
    XGA(1024, 768),
    UXGA(1600, 1200),
    QXGA(2048, 1536),

    // 16/9 resolutions
    HD_720(1280, 720),
    HD_1080(1920, 1080),
    QHD(2560, 1440),
    UHD_4K(3840, 2160),
    UHD_5K(5120, 2880),
    UHD_8K(7680, 4320),
    ;

    fun asVector2(): Vector2 {
        return Vector2(width.toDouble(), height.toDouble())
    }

    fun asRectangle(): Rectangle {
        return Rectangle(Vector2.NULL, asVector2())
    }

    fun asVector3(): Vector3 {
        return Vector3(width.toDouble(), height.toDouble(), 0.0)
    }

    fun asBox(): Box {
        return Box(Vector3.NULL, asVector3())
    }

    // region Any

    override fun toString(): String {
        return "$name ($width⨉$height)"
    }

    // endregion
}

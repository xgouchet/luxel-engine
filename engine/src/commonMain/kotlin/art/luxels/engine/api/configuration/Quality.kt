package art.luxels.engine.api.configuration

/**
 * The quality of the simulation.
 * This directly correlates with the amount of time needed to compute the simulation.
 * @property count the number of luxel being spawned in the simulation
 */
@Suppress("UndocumentedPublicProperty")
enum class Quality(val count: Long) {
    // ROUGH = n * 1
    // GRAINY = n * 1_000
    // CLEAR = n * 1_000_000
    // SHARP = n * 1_000_000_000
    ROUGH_1(0x1L),
    ROUGH_8(0x8L),
    ROUGH_10(0x10L),
    ROUGH_80(0x80L),
    ROUGH_100(0x100L),
    ROUGH_800(0x800L),
    GRAINY_1(0x1_000L),
    GRAINY_8(0x8_000L),
    GRAINY_10(0x10_000L),
    GRAINY_80(0x80_000L),
    GRAINY_100(0x100_000L),
    GRAINY_800(0x800_000L),
    CLEAR_1(0x1_000_000L),
    CLEAR_8(0x8_000_000L),
    CLEAR_10(0x10_000_000L),
    CLEAR_80(0x80_000_000L),
    CLEAR_100(0x100_000_000L),
    CLEAR_800(0x800_000_000L),
    SHARP_1(0x1_000_000_000L),
    SHARP_8(0x8_000_000_000L),
}

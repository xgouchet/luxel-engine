package fr.xgouchet.luxels.engine.api.configuration

/**
 * The quality of the simulation.
 * This directly correlates with the amount of time needed to compute the simulation.
 * @property count the number of luxel being spawned in the simulation
 */
enum class Quality(val count: Long) {
    SINGLE(0x1L),
    SPARSE(0x100L),
    DEBUG(0x800L),
    ROUGH(0x1_000L),
    SAMPLE(0x8_000L),
    PREVIEW(0x10_000L),
    DRAFT(0x80_000L),
    SKETCH(0x100_000L),
    LOW(0x800_000L),
    MEDIUM(0x1_000_000L),
    GOOD(0x8_000_000L),
    PREPROD(0x10_000_000L),
    PROD(0x80_000_000L),
    HIGH(0x100_000_000L),
    SHARP(0x800_000_000L),
    PERFECT(0x1_000_000_000L),
    OVERKILL(0x8_000_000_000L),
}

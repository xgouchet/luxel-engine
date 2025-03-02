package art.luxels.engine.api.configuration

/**
 * The type of simulation to perform.
 */
enum class SimulationType {
    /** Renders the simulation in full color. */
    RENDER,

    /** Debug Pass: renders only the spawn location of luxels in bright green. */
    SPAWN,

    /** Debug Pass: renders only the path of luxels in bright blue. */
    PATH,

    /** Debug Pass: renders only the death location of luxels in bright red. */
    DEATH,

    /**
     * Debug Pass: renders the simulation environment color.
     */
    ENV,
}

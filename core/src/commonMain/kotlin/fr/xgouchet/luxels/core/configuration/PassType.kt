package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.simulation.Simulator

/**
 * The type of pass to perform.
 */
enum class PassType {
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
     * @see [Simulator.environmentColor]
     */
    ENV,
}

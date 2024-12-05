package fr.xgouchet.luxels.core.simulation

import fr.xgouchet.luxels.core.configuration.Configuration
import fr.xgouchet.luxels.core.configuration.input.InputData
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.model.Luxel

interface SimulationRunner {

    suspend fun <D : Dimension, L : Luxel<D>, I : Any> runSimulationWithInput(
        simulator: Simulator<D, L, I>,
        configuration: Configuration<D, I>,
        threadCount: Int,
        inputData: InputData<I>,
    )
}
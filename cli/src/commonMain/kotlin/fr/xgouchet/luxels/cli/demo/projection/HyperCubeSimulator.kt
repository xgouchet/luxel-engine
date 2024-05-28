package fr.xgouchet.luxels.cli.demo.projection

import fr.xgouchet.luxels.core.math.Dimension

internal class HyperCubeSimulator : NdCubeSimulator<Dimension.D4>() {

    // region Simulator

    override fun outputName(): String {
        return "hypercube"
    }

    // endregion
}

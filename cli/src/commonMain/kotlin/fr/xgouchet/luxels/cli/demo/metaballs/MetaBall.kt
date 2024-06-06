package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.luxels.components.animation.AnimatedVector
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector

data class MetaBall(
    val position: Vector<Dimension.D3>,
    val radius: Double
)

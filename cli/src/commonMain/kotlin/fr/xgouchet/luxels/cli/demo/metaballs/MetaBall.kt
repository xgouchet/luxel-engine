package fr.xgouchet.luxels.cli.demo.metaballs

import fr.xgouchet.graphikio.color.HDRColor
import fr.xgouchet.luxels.components.animation.AnimatedVector
import fr.xgouchet.luxels.core.math.Dimension

data class MetaBall(
    val position: AnimatedVector<Dimension.D3>,
    val radius: Double,
    val color: HDRColor,
)

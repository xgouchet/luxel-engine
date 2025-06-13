package art.luxels.scenes.demo.metaballs

import art.luxels.components.animation.AnimatedVector
import art.luxels.core.math.Dimension
import art.luxels.imageio.color.HDRColor

data class MetaBall(val position: AnimatedVector<Dimension.D3>, val radius: Double, val color: HDRColor)

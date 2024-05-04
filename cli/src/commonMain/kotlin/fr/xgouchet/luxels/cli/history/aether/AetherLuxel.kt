package fr.xgouchet.luxels.cli.history.aether

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.math.TAU
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.SimplePositionSource
import kotlin.math.cos

internal class AetherLuxel(
    val curve3: Curve<Vector3>,
    waveLength: WavelengthNanometer,
    lifespan: Int,
) : PrincipledLuxel<EMSColorSource, SimplePositionSource, AgeingLifespanSource>(
    EMSColorSource(waveLength),
    SimplePositionSource(Vector3.NULL),
    AgeingLifespanSource(lifespan),
) {

    // region Luxel

    override fun position(): Vector3 {
        val progress = lifespanSource.progression
        return curve3.getPosition(progress)
    }

    override fun color(): Color {
        val rawColor = super.color()
        val progress = lifespanSource.progression
        val factor = (1.0 - cos(progress * TAU)) / 2.0

        return rawColor * factor
    }

    // endregion
}

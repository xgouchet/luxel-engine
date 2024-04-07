package fr.xgouchet.luxels.cli.history.aether

import fr.xgouchet.luxels.components.color.EMSColorSource
import fr.xgouchet.luxels.components.color.WavelengthNanometer
import fr.xgouchet.luxels.components.geometry.Curve
import fr.xgouchet.luxels.core.color.Color
import fr.xgouchet.luxels.core.math.geometry.Vector3
import fr.xgouchet.luxels.core.model.AgeingLifespanSource
import fr.xgouchet.luxels.core.model.PrincipledLuxel
import fr.xgouchet.luxels.core.position.StaticPositionSource

internal class AetherLuxel(
    val curve3: Curve<Vector3>,
    waveLength: WavelengthNanometer,
    lifespan: Int,
) : PrincipledLuxel<EMSColorSource, StaticPositionSource, AgeingLifespanSource>(
    EMSColorSource(waveLength),
    StaticPositionSource(Vector3.NULL),
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

        return if (progress < 0.1) {
            rawColor * progress * 10.0
        } else if (progress > 0.9) {
            rawColor * ((1.0 - progress) * 10.0)
        } else {
            rawColor
        }
    }

    // endregion
}

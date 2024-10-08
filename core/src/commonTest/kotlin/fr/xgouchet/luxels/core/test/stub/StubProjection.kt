package fr.xgouchet.luxels.core.test.stub

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.projection.Projection

class StubProjection<D : Dimension> : Projection<D> {

    override val simulationSpace: Volume<D>
        get() = TODO("Not yet implemented")
    override val filmSpace: Volume<Dimension.D2>
        get() = TODO("Not yet implemented")

    override fun convertPosition(position: Vector<D>): Vector<Dimension.D2> {
        TODO("Not yet implemented")
    }
}

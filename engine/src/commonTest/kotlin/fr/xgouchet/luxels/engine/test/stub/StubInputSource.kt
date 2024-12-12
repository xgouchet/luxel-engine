package fr.xgouchet.luxels.engine.test.stub

import fr.xgouchet.luxels.engine.api.input.InputData
import fr.xgouchet.luxels.engine.api.input.InputSource

class StubInputSource<I : Any>(
    val rawData: List<I>,
) : InputSource<I>() {
    override val inputDataList: List<InputData<I>> = rawData.mapIndexed { idx, it -> stubInputData(idx, it) }
}

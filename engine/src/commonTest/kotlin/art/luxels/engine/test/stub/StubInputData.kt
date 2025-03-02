package art.luxels.engine.test.stub

import art.luxels.engine.api.input.InputData

fun <I : Any> stubInputData(idx: Int, input: I): InputData<I> = InputData("#$idx", idx.toLong(), input)

package fr.xgouchet.luxels.engine.test.stub

import fr.xgouchet.luxels.engine.api.input.InputData

fun <I : Any> stubInputData(idx: Int, input: I): InputData<I> = InputData("#$idx", idx.toLong(), input)

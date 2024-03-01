package fr.xgouchet.luxels.core.concurrent

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val mainDispatcher: CoroutineContext = Dispatchers.Default
actual val computeDispatcher: CoroutineContext = Dispatchers.Unconfined
actual val ioDispatcher: CoroutineContext = Dispatchers.IO

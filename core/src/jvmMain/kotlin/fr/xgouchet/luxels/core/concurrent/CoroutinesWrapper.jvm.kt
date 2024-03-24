@file:Suppress("InjectDispatcher")

package fr.xgouchet.luxels.core.concurrent

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/** The main dispatcher. */
actual val mainDispatcher: CoroutineContext = Dispatchers.Default

/** The computing dispatcher. */
actual val computeDispatcher: CoroutineContext = Dispatchers.Unconfined

/** The I/O dispatcher. */
actual val ioDispatcher: CoroutineContext = Dispatchers.IO

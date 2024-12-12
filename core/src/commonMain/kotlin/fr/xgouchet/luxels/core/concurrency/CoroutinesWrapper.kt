package fr.xgouchet.luxels.core.concurrency

import kotlin.coroutines.CoroutineContext

/** The main dispatcher. */
expect val mainDispatcher: CoroutineContext

/** The computing dispatcher. */
expect val computeDispatcher: CoroutineContext

/** The I/O dispatcher. */
expect val ioDispatcher: CoroutineContext

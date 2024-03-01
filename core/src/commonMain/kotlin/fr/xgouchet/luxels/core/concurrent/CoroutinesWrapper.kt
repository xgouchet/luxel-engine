package fr.xgouchet.luxels.core.concurrent

import kotlin.coroutines.CoroutineContext

expect val mainDispatcher: CoroutineContext
expect val computeDispatcher: CoroutineContext
expect val ioDispatcher: CoroutineContext

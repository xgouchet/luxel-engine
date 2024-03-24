package fr.xgouchet.luxels.core.test.kotest

import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

class MemoryGuardExtension : BeforeEachListener, AfterEachListener {

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        cleanupMemory()
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)
        cleanupMemory()
    }

    // region Internal

    @Suppress("ExplicitGarbageCollectionCall")
    private fun cleanupMemory() {
        val runtime = Runtime.getRuntime()
        println(" pre-GC / free:${runtime.freeMemory()} total:${runtime.totalMemory()} max:${runtime.maxMemory()}")
        System.gc()
        println("post-GC / free:${runtime.freeMemory()} total:${runtime.totalMemory()} max:${runtime.maxMemory()}")
    }

    // endregion
}

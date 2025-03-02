package art.luxels.components.test.kotest

import io.kotest.common.KotestInternal
import io.kotest.core.listeners.AfterEachListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

@OptIn(KotestInternal::class)
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
        System.gc()
    }

    // endregion
}

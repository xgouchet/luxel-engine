package art.luxels.imageio.test.kotest.extensions

import io.kotest.common.KotestInternal
import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import okio.fakefilesystem.FakeFileSystem

@OptIn(KotestInternal::class)
class OkioExtension : AfterTestListener {
    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        FakeFileSystem().checkNoOpenFiles()
    }

    companion object {
        val FILESYSTEM: FakeFileSystem = FakeFileSystem()
    }
}

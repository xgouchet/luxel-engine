package fr.xgouchet.graphikio.test.kotest.extensions

import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import okio.fakefilesystem.FakeFileSystem

class OkioExtension : AfterTestListener {

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        FakeFileSystem().checkNoOpenFiles()
    }

    companion object {
        val FILESYSTEM: FakeFileSystem = FakeFileSystem()
    }
}

package fr.xgouchet.luxels.core.log

import dev.mokkery.answering.calls
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.not
import dev.mokkery.verify.VerifyMode.Companion.order
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class StdOutLogHandlerSpec : DescribeSpec(
    {
        describe("onLog [no progress]") {
            it("prints a Verbose log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.verbose(message)

                    verify {
                        output.invoke("\r… $message\n")
                    }
                }
            }

            it("prints a Verbose log within section") {
                checkAll(Arb.string(), Arb.string()) { message, section ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startSection(section)
                    logHandler.verbose(message)
                    logHandler.endSection()

                    verify(order) {
                        output.invoke("\r- $section\n")
                        output.invoke("\r  … $message\n")
                    }
                }
            }

            it("prints a Debug log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.debug(message)

                    verify {
                        output.invoke("\r⚙ $message\n")
                    }
                }
            }

            it("prints a Debug log within section") {
                checkAll(Arb.string(), Arb.string()) { message, section ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startSection(section)
                    logHandler.debug(message)
                    logHandler.endSection()

                    verify(order) {
                        output.invoke("\r- $section\n")
                        output.invoke("\r  ⚙ $message\n")
                    }
                }
            }

            it("prints a Info log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.info(message)

                    verify {
                        output.invoke("\rℹ $message\n")
                    }
                }
            }

            it("prints a Info log within section") {
                checkAll(Arb.string(), Arb.string()) { message, section ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startSection(section)
                    logHandler.info(message)
                    logHandler.endSection()

                    verify(order) {
                        output.invoke("\r- $section\n")
                        output.invoke("\r  ℹ $message\n")
                    }
                }
            }

            it("prints a Warning log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.warning(message)

                    verify {
                        output.invoke("\r⚠ $message\n")
                    }
                }
            }

            it("prints a Warning log within section") {
                checkAll(Arb.string(), Arb.string()) { message, section ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startSection(section)
                    logHandler.warning(message)
                    logHandler.endSection()

                    verify(order) {
                        output.invoke("\r- $section\n")
                        output.invoke("\r  ⚠ $message\n")
                    }
                }
            }

            it("prints an Error log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.error(message)

                    verify {
                        output.invoke("\r☢ $message\n")
                    }
                }
            }

            it("prints an Error log within section") {
                checkAll(Arb.string(), Arb.string()) { message, section ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startSection(section)
                    logHandler.error(message)
                    logHandler.endSection()

                    verify(order) {
                        output.invoke("\r- $section\n")
                        output.invoke("\r  ☢ $message\n")
                    }
                }
            }

            it("does nothing on endProgress") {
                val output = mock<Output> { every { invoke(any()) } calls {} }
                val logHandler = StdOutLogHandler(output)

                logHandler.endProgress()

                verify(not) { output.invoke(any()) }
            }

            it("does nothing on progress") {
                checkAll(Arb.string(), Arb.double()) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.progress(progress, message)

                    verify(not) { output.invoke(any()) }
                }
            }
        }

        describe("onLog [in progress]") {
            it("doesn't print a Verbose log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.verbose(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("doesn't print a Debug log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.debug(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("doesn't print a Info log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.info(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("doesn't print a Warning log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.warning(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("doesn't print an Error log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.error(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("doesn't print a StartSection log") {
                checkAll(Arb.string()) { message ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)

                    logHandler.startProgress()
                    logHandler.error(message)

                    verify(not) { output.invoke(any()) }
                }
            }

            it("prints a progress log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.progress(progressDouble, message)

                    verify { output.invoke("\r… $progress‰ $message") }
                }
            }
        }

        describe("onLog [after end progress]") {
            it("prints a Verbose log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.verbose(message)
                    logHandler.progress(progressDouble, "first")
                    logHandler.endProgress()

                    verify(order) {
                        output.invoke("\r… $progress‰ first")
                        output.invoke("\r… $message\n")
                    }
                }
            }

            it("prints a Debug log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.debug(message)
                    logHandler.progress(progressDouble, "first")
                    logHandler.endProgress()

                    verify(order) {
                        output.invoke("\r… $progress‰ first")
                        output.invoke("\r⚙ $message\n")
                    }
                }
            }

            it("prints a Info log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.info(message)
                    logHandler.progress(progressDouble, "first")
                    logHandler.endProgress()

                    verify(order) {
                        output.invoke("\r… $progress‰ first")
                        output.invoke("\rℹ $message\n")
                    }
                }
            }

            it("prints a Warning log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.warning(message)
                    logHandler.progress(progressDouble, "first")
                    logHandler.endProgress()

                    verify(order) {
                        output.invoke("\r… $progress‰ first")
                        output.invoke("\r⚠ $message\n")
                    }
                }
            }

            it("prints an Error log") {
                checkAll(Arb.string(), Arb.int(min = 0, max = 1000)) { message, progress ->
                    val output = mock<Output> { every { invoke(any()) } calls {} }
                    val logHandler = StdOutLogHandler(output)
                    val progressDouble = progress / 1000.0

                    logHandler.startProgress()
                    logHandler.error(message)
                    logHandler.progress(progressDouble, "first")
                    logHandler.endProgress()

                    verify(order) {
                        output.invoke("\r… $progress‰ first")
                        output.invoke("\r☢ $message\n")
                    }
                }
            }
        }
    },
)

package fr.xgouchet.luxels.core.log

import fr.xgouchet.luxels.core.test.stub.StubLogHandler
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class LoggerTest : DescribeSpec(
    {
        describe("a logger handling messages") {
            it("forwards verbose log to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.verbose(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Message(Log.Level.VERBOSE, msg)
                }
            }

            it("forwards debug log to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.debug(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Message(Log.Level.DEBUG, msg)
                }
            }

            it("forwards info log to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.info(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Message(Log.Level.INFO, msg)
                }
            }

            it("forwards warning log to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.warning(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Message(Log.Level.WARNING, msg)
                }
            }

            it("forwards error log to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.error(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Message(Log.Level.ERROR, msg)
                }
            }
        }

        describe("a logger handling progress") {
            it("forwards start progress to handler") {
                val stubLogHandler = StubLogHandler()
                val logger = Logger(stubLogHandler)

                logger.startProgress()

                val logs = stubLogHandler.getLogs()
                logs should haveSize(1)
                logs[0] shouldBe Log.StartProgress
            }

            it("forwards progress to handler") {
                checkAll(Arb.double(0.0, 1.0), Arb.string()) { p, msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.progress(p, msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.Progress(p, msg)
                }
            }

            it("forwards end progress to handler") {
                val stubLogHandler = StubLogHandler()
                val logger = Logger(stubLogHandler)

                logger.endProgress()

                val logs = stubLogHandler.getLogs()
                logs should haveSize(1)
                logs[0] shouldBe Log.EndProgress
            }
        }

        describe("a logger handling sections") {
            it("forwards section start to handler") {
                checkAll(Arb.string()) { msg ->
                    val stubLogHandler = StubLogHandler()
                    val logger = Logger(stubLogHandler)

                    logger.startSection(msg)

                    val logs = stubLogHandler.getLogs()
                    logs should haveSize(1)
                    logs[0] shouldBe Log.StartSection(msg)
                }
            }

            it("forwards section end to handler") {
                val stubLogHandler = StubLogHandler()
                val logger = Logger(stubLogHandler)

                logger.endSection()

                val logs = stubLogHandler.getLogs()
                logs should haveSize(1)
                logs[0] shouldBe Log.EndSection
            }
        }
    },
)

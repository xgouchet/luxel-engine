package art.luxels.core.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class AgeingLifespanSourceSpec : DescribeSpec(
    {
        describe("constructor") {
            it("fails with negative lifespan") {
                checkAll(Arb.int(max = -1)) { lifespan ->
                    shouldThrow<IllegalArgumentException> {
                        AgeingLifespanSource(lifespan)
                    }
                }
            }

            it("fails with null lifespan") {
                shouldThrow<IllegalArgumentException> {
                    AgeingLifespanSource(0)
                }
            }
        }

        describe("age") {
            it("always starts at 0") {
                checkAll(lifespanArb()) { lifespan ->
                    val source = AgeingLifespanSource(lifespan)

                    source.age shouldBe 0
                }
            }

            it("updates with onStep") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(step)

                    source.age shouldBe step
                }
            }

            it("resets with onStart") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(step)
                    source.onStart()

                    source.age shouldBe 0
                }
            }
        }

        describe("progression") {
            it("always starts at 0") {
                checkAll(lifespanArb()) { lifespan ->
                    val source = AgeingLifespanSource(lifespan)

                    source.progression shouldBe 0.0
                }
            }

            it("is 1.0 when step = lifespan") {
                checkAll(lifespanArb()) { lifespan ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(lifespan)

                    source.progression shouldBe 1.0
                }
            }

            it("resets with onStart") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(step)
                    source.onStart()

                    source.progression shouldBe 0.0
                }
            }
        }

        describe("isAlive") {
            it("always starts true") {
                checkAll(lifespanArb()) { lifespan ->
                    val source = AgeingLifespanSource(lifespan)

                    source.isAlive() shouldBe true
                }
            }

            it("is true when step < lifespan") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    if (step < lifespan) {
                        source.onStep(step)
                    }

                    source.isAlive() shouldBe true
                }
            }

            it("is true after onStart") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(step)
                    source.onStart()

                    source.isAlive() shouldBe true
                }
            }

            it("is true after onStart (even after kill)") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.kill()
                    source.onStart()

                    source.isAlive() shouldBe true
                }
            }

            it("is false when step > lifespan") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    if (step > lifespan) {
                        source.onStep(step)
                    } else {
                        source.onStep(lifespan + 1)
                    }

                    source.isAlive() shouldBe false
                }
            }

            it("is false after kill") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    source.onStep(step)
                    source.kill()

                    source.isAlive() shouldBe false
                }
            }
        }
        describe("onEnd") {
            it("does nothing") {
                checkAll(lifespanArb(), Arb.int(min = 1)) { lifespan, step ->
                    val source = AgeingLifespanSource(lifespan)

                    if (step < lifespan) {
                        source.onStep(step)
                    }
                    val age = source.age
                    val progress = source.progression
                    val isAlive = source.isAlive()
                    source.onEnd()

                    source.age shouldBe age
                    source.progression shouldBe progress
                    source.isAlive() shouldBe isAlive
                }
            }
        }
    },
)

private fun lifespanArb() = Arb.int(min = 1, max = 65536)

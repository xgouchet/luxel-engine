package art.luxels.core.utils

import art.luxels.core.test.kotest.property.nonEmptyStringCollectionArb
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import kotlin.math.min

class CollectionExtSpec : DescribeSpec(
    {

        describe("three-way zip (on string collections)") {
            val transform: (String, String, String) -> String = { a, b, c -> a + b + c }

            it("returns an empty collection when at least one input is empty") {
                checkAll(nonEmptyStringCollectionArb(), nonEmptyStringCollectionArb()) { x, y ->
                    val zippedEmptyA = zip(emptyList(), x, y, transform)
                    val zippedEmptyB = zip(y, emptyList(), x, transform)
                    val zippedEmptyC = zip(x, y, emptyList(), transform)

                    zippedEmptyA should beEmpty()
                    zippedEmptyB should beEmpty()
                    zippedEmptyC should beEmpty()
                }
            }

            it("returns a zipped collection") {
                checkAll(
                    nonEmptyStringCollectionArb(),
                    nonEmptyStringCollectionArb(),
                    nonEmptyStringCollectionArb(),
                ) { a, b, c ->
                    val expectedSize = min(a.size, min(b.size, c.size))
                    val zipped = zip(a, b, c, transform)
                    val lastIndex = zipped.lastIndex

                    zipped should haveSize(expectedSize)
                    zipped[0] shouldBe transform(a[0], b[0], c[0])
                    zipped[lastIndex] shouldBe transform(a[lastIndex], b[lastIndex], c[lastIndex])
                }
            }
        }
    },
)

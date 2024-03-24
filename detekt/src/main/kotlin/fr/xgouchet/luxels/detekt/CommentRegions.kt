package fr.xgouchet.luxels.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.api.internal.RequiresTypeResolution
import io.gitlab.arturbosch.detekt.rules.fqNameOrNull
import io.gitlab.arturbosch.detekt.rules.isInternal
import io.gitlab.arturbosch.detekt.rules.isOperator
import io.gitlab.arturbosch.detekt.rules.isOverride
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.SimpleType
import org.jetbrains.kotlin.types.typeUtil.supertypes

/**
 * A rule to detekt comment code regions.
 * @active
 */
@RequiresTypeResolution
class CommentRegions(
    ruleSetConfig: Config,
) : Rule(ruleSetConfig) {

    private val forceRegionOnMainTypeFunctions: Boolean by config(defaultValue = false)
    private val customRegionOnMainTypeFunctions: Boolean by config(defaultValue = true)

    // TODO unexpected regions for class functions

    override val issue: Issue = Issue(
        javaClass.simpleName,
        Severity.Minor,
        "This rule reports functions not wrapped in the appropriate code regions.",
        Debt.FIVE_MINS,
    )

    override fun visitFile(file: PsiFile) {
        if (bindingContext == BindingContext.EMPTY) {
            println("ERROR, MISSING BINDING CONTEXT")
            return
        }
        super.visitFile(file)
        if (region != null) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(file),
                    "Unclosed region comment at the EOF: $region",
                ),
            )
        }
    }

    private val superTypesStack = mutableListOf<Collection<KotlinType>>()
    private val currentTypeStack = mutableListOf<SimpleType>()
    private var region: String? = null

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        if (!region.isNullOrBlank()) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(classOrObject),
                    "Class or Object declaration should not happen within a region block",
                ),
            )
        }

        val resolvedType = bindingContext[BindingContext.CLASS, classOrObject]?.defaultType
        if (resolvedType == null) {
            println("Unresolved ${classOrObject.name}")
            return
        }

        currentTypeStack.add(resolvedType)
        superTypesStack.add(resolvedType.supertypes())

        super.visitClassOrObject(classOrObject)

        currentTypeStack.removeLast()
        superTypesStack.removeLast()
    }

    override fun visitComment(comment: PsiComment) {
        val commentContent = comment.text

        val matchResult = START_REGION_REGEX.matchEntire(commentContent)
        if (matchResult != null) {
            if (!region.isNullOrBlank()) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(comment),
                        "New region block comment but the previous region '$region' was not closed",
                    ),
                )
            }
            region = matchResult.groupValues[1]
        } else if (END_REGION_REGEX.matches(commentContent)) {
            region = null
        }

        super.visitComment(comment)
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val currentType = currentTypeStack.lastOrNull()
        if (currentType != null) {
            visitNamedFunctionInType(currentType, function)
        }
    }

    private fun visitNamedFunctionInType(
        currentType: SimpleType,
        function: KtNamedFunction,
    ) {
        val currentTypeRegion = currentType.fqNameOrNull()?.shortName()?.asString() ?: "???"
        var expectedRegion: String = currentTypeRegion

        if (function.isOverride()) {
            val superTypes = superTypesStack.lastOrNull().orEmpty()
            superTypes.forEach { superType ->
                if (superType.memberScope.getFunctionNames().any { it == function.nameAsSafeName }) {
                    expectedRegion = superType.fqNameOrNull()?.shortName()?.asString() ?: "???"
                }
            }
        } else if (function.isOperator()) {
            expectedRegion = REGION_OPERATORS
        } else if (function.isInternal() || function.isPrivate()) {
            expectedRegion = REGION_INTERNAL
        }

        val actualRegion = region
        val isMainRegion = (expectedRegion == currentTypeRegion)
        if (actualRegion.isNullOrBlank()) {
            verifyFunctionOutsideRegion(expectedRegion, isMainRegion, function)
        } else {
            verifyFunctionWithinRegion(expectedRegion, actualRegion, isMainRegion, function)
        }
    }

    private fun verifyFunctionOutsideRegion(
        expectedRegion: String,
        isMainRegion: Boolean,
        function: KtNamedFunction,
    ) {
        if (isMainRegion && forceRegionOnMainTypeFunctions) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(function),
                    "Public function declaration should not happen outside a region block; " +
                        "was expected in a region named $expectedRegion. You can turn off this rule by setting the " +
                        "CommentRegions.forceRegionOnMainTypeFunctions option to false.",
                ),
            )
        } else if (!isMainRegion) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(function),
                    "Function declaration should not happen outside a region block; function ${function.name} " +
                        "should be in a region named $expectedRegion.",
                ),
            )
        }
    }

    private fun verifyFunctionWithinRegion(
        expectedRegion: String,
        actualRegion: String,
        isMainRegion: Boolean,
        function: KtNamedFunction,
    ) {
        if (actualRegion != expectedRegion) {
            if (expectedRegion == REGION_INTERNAL) {
                if (!actualRegion.startsWith(REGION_INTERNAL)) {
                    val visibility =
                        if (function.isPrivate()) "private" else if (function.isInternal()) "internal" else "?"
                    report(
                        CodeSmell(
                            issue,
                            Entity.from(function),
                            "Function declaration with visibility $visibility should be in a region " +
                                "prefixed with $REGION_INTERNAL;  function ${function.name} was found in region named " +
                                "$actualRegion instead.",
                        ),
                    )
                }
            } else if ((!isMainRegion) || (!customRegionOnMainTypeFunctions)) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(function),
                        "Function declaration should be in a region named $expectedRegion; " +
                            "function ${function.name} was found in region named $actualRegion instead.",
                    ),
                )
            }
        }
    }

    companion object {

        private const val REGION_OPERATORS = "Operators"
        private const val REGION_INTERNAL = "Internal"

        private val START_REGION_REGEX = Regex("""//\s*region\s+(.*)""")
        private val END_REGION_REGEX = Regex("""//\s*endregion\s*""")
    }
}

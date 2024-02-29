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
//            println("REGION: ${matchResult.groupValues[1]} $commentContent")
            region = matchResult.groupValues[1]
        } else if (END_REGION_REGEX.matches(commentContent)) {
            region = null
        }

        super.visitComment(comment)
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val currentType = currentTypeStack.lastOrNull()
        if (currentType == null) {
            // Top level function, ignore ?
            return
        }

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
            expectedRegion = "Operators"
        } else if (function.isInternal() || function.isPrivate()) {
            expectedRegion = "Internal"
        }

        if (expectedRegion == currentTypeRegion) {
            if (region.isNullOrBlank() && forceRegionOnMainTypeFunctions) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(function),
                        "Function declaration should not happen outside a region block; was expected in a region named $expectedRegion.",
                    ),
                )
            } else if (region != expectedRegion && !customRegionOnMainTypeFunctions) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(function),
                        "Function declaration in region $region but was expected in a region named $expectedRegion.",
                    ),
                )
            }
        } else if (region != expectedRegion) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(function),
                    "Function declaration in region $region but was expected in a region named $expectedRegion.",
                ),
            )
        }
    }

    companion object {
        private val START_REGION_REGEX = Regex("""//\s*region\s+(.*)""")
        private val END_REGION_REGEX = Regex("""//\s*endregion\s*""")
    }
}

package fr.xgouchet.luxels.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

/**
 * The [RuleSetProvider] for Datadog's SDK for Android.
 */
class LuxelDetektProvider : RuleSetProvider {
    override val ruleSetId: String = "luxel"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                CommentRegions(config),
            ),
        )
    }
}

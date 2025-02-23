@file:Suppress("MatchingDeclarationName")

package fr.xgouchet.luxels.core.system

import fr.xgouchet.luxels.core.concurrency.ConcurrencyCapabilities
import fr.xgouchet.luxels.core.render.Resolution
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * A class giving access to runtime system information.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object SystemInfo : ConcurrencyCapabilities {

    // region ConcurrencyCapabilities

    @OptIn(ExperimentalForeignApi::class)
    actual override fun getCpuParallelCapacity(): Int {
        return 1
    }

    actual override fun getMemoryParallelCapacity(resolution: Resolution): Int {
        return 1
    }

    // endregion

    actual fun clearMemory() {
        // No-op
    }
}

package art.luxels.cli.wip.rain

import art.luxels.cli.common.LongInputSceneCommand
import art.luxels.core.math.Dimension.D2
import art.luxels.engine.api.Scene

// 5525e11f
// 5b39ec29
// 8edb2bb8
// ae8469fd
// aeebc9e1
// e5d8d2fe
// 6bec6b51
// 7c0e9028
class RainCommand : LongInputSceneCommand<D2, RainLuxel, RainEnvironment>("rain", D2) {
    // region AbstractSceneCommand

    override fun getScene(): Scene<D2, RainLuxel, Long, RainEnvironment> {
        return RainScene()
    }

    // endregion
}
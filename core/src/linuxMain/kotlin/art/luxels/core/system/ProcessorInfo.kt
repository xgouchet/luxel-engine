package art.luxels.core.system

data class ProcessorInfo(
    val processorId: Int,
    var vendorId: String,
    var modelName: String,
    var cores: Int,
)

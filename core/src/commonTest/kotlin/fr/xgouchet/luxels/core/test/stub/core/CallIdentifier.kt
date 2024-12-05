package fr.xgouchet.luxels.core.test.stub.core

data class CallIdentifier(
    val name: String,
    val params: Map<String, Any?>,
) {
    fun matches(callIdentifier: CallIdentifier): Boolean {
        val matchName = callIdentifier.name == name
        val matchParams = callIdentifier.params.all {
            // TODO create advanced matchers
            params[it.key] == it.value
        }
        return matchName && matchParams
    }

    override fun toString(): String {
        return "$name(${params.entries.joinToString { "${it.key}: ${it.value}" }})"
    }

    fun getParamsDiff(callIdentifier: CallIdentifier): String {
        return callIdentifier.params.mapNotNull { (k,v) ->
            if (params[k] == callIdentifier.params[k]){
                null
            } else {
                "[$k]: ${params[k]} != ${callIdentifier.params[k]}"
            }
        }.joinToString()
    }
}

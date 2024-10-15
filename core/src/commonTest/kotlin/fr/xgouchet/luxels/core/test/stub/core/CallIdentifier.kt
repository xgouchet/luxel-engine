package fr.xgouchet.luxels.core.test.stub.core

data class CallIdentifier(
    val name: String,
    val params: Map<String, Any?>,
) {
    fun matches(callIdentifier: CallIdentifier): Boolean {
        val matchName = callIdentifier.name == name
        val matchParams = params.all {
            // TODO create advanced matchers
            callIdentifier.params[it.key] == it.value
        }
        return matchName && matchParams
    }

    override fun toString(): String {
        return "$name(${params.entries.joinToString { "${it.key}: ${it.value}" }})"
    }
}

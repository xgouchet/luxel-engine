@file:OptIn(ExperimentalSerializationApi::class)

package fr.xgouchet.luxels.engine.api.input

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType

internal fun KType.enumValueOf(name: String, serializer: KSerializer<Any?> = serializer(this)): Enum<*>? {
    val decoded = Json.decodeFromString(serializer, "\"$name\"")
    return decoded as? Enum<*>
}

internal fun KType.enumValuesName(serializer: KSerializer<Any?> = serializer(this)): List<String> {
    val enumName = serializer.descriptor.serialName
    return serializer.descriptor.elementNames.map { it.removePrefix(enumName) }
}

internal fun KType.enumValues(serializer: KSerializer<Any?> = serializer(this)): List<Enum<*>> {
    return enumValuesName(serializer).mapNotNull { enumValueOf(it, serializer) }
}

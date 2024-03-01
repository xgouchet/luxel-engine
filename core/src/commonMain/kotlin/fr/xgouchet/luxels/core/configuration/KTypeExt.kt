@file:OptIn(ExperimentalSerializationApi::class)

package fr.xgouchet.luxels.core.configuration

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType

internal fun KType.enumValueOf(name: String, serializer: KSerializer<Any?> = serializer(this)): Enum<*> {
    if (serializer.descriptor.kind != SerialKind.ENUM) throw error("enumValueOf must be used on enum")
    return Json.decodeFromString(serializer, "\"$name\"") as Enum<*>
}

internal fun KType.enumValuesName(serializer: KSerializer<Any?> = serializer(this)): List<String> {
    if (serializer.descriptor.kind != SerialKind.ENUM) throw error("enumValuesName must be used on enum")
    val enumName = serializer.descriptor.serialName
    return serializer.descriptor.elementNames.map { it.removePrefix(enumName) }
}

internal fun KType.enumValues(serializer: KSerializer<Any?> = serializer(this)): List<Enum<*>> {
    if (serializer.descriptor.kind != SerialKind.ENUM) throw error("enumValues must be used on enum")
    return enumValuesName(serializer).map { enumValueOf(it, serializer) }
}

package art.luxels.engine.api.input

import kotlin.reflect.KType

/**
 * An [InputSource] based of all the values of an Enum.
 * @param E the type of the enum
 * @param enumType the [KType] for the enum (i.e.: `typeOf<MyEnum>()`)
 */
class EnumInputSource<E : Enum<E>>(enumType: KType) : InputSource<E>() {
    override val inputDataList: List<InputData<E>> = enumType.enumValues().map {
        @Suppress("UNCHECKED_CAST")
        InputData(it.name, it.ordinal.toLong(), it as E)
    }
}

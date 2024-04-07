package fr.xgouchet.luxels.core.collections

import kotlin.math.min

inline fun <A, B, C, O> zip(
    collecA: Collection<A>,
    collecB: Collection<B>,
    collecC: Collection<C>,

    transform: (a: A, b: B, c: C) -> O,
): List<O> {
    val iteratorA = collecA.iterator()
    val iteratorB = collecB.iterator()
    val iteratorC = collecC.iterator()
    val outSize = min(collecA.size, min(collecB.size, collecC.size))

    val list = ArrayList<O>(outSize)
    while (iteratorA.hasNext() && iteratorB.hasNext() && iteratorC.hasNext()) {
        list.add(
            transform(iteratorA.next(), iteratorB.next(), iteratorC.next()),
        )
    }

    return list
}

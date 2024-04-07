package fr.xgouchet.luxels.core.collections

import kotlin.math.min

/**
 * Utility method to zip three collections together.
 * @param A the type of elements in the first collection
 * @param B the type of elements in the second collection
 * @param C the type of elements in the third collection
 * @param O the type of elements in the output collection
 * @param collecA the first collection
 * @param collecB the second collection
 * @param collecC the third collection
 * @param transform the transformation to apply
 * @return a List of elements created by combining elements from the three input collections.
 * The size of the output list correspond to the size of the smallest input.
 */
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

package fr.xgouchet.graphikio.data

/**
 * Strategy used to resolve reading/writing data from/to a raster with invalid address.
 */
enum class OutOfBoundStrategy {
    /** Throw an exception. */
    FAIL,

    /** Clamp to the image boundaries. */
    CLAMP,

    /** Loop over the image (without reflections). */
    LOOP,
}

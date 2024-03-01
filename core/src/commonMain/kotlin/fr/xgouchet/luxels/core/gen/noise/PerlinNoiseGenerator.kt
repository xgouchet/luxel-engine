package fr.xgouchet.luxels.core.gen.noise

import fr.xgouchet.luxels.core.math.Interpolation
import kotlin.math.floor

/**
 * Provides a noise based on the algorithm by Ken Perlin.
 */
class PerlinNoiseGenerator(
    private val interpolation: Interpolation = Interpolation.Quintic,
) : DimensionalNoiseGenerator {

    // region DimensionalNoise

    override fun noise(input: List<Double>, outputSize: Int): List<Double> {
        maxSum = 0.0
        val floor = input.map { floor(it) }
        val frac = input.zip(floor) { a, b -> a - b }
        val ref = floor.map { perlinHash(it) }

        val fade = frac.map { interpolation.factor(it) }
        val path = IntArray(ref.size) { 0 }.toList()

        val res = interpolate(
            idx = 0,
            hash = ref,
            values = frac,
            path = path,
            fade = fade,
            outputSize = outputSize,
        )
        return res
    }

    // endregion

    // region Internal

    @Suppress("NamedArguments", "LongParameterList")
    private fun interpolate(
        idx: Int,
        hash: List<Int>,
        values: List<Double>,
        path: List<Int>,
        fade: List<Double>,
        outputSize: Int,
    ): List<Double> {
        check(hash.size == values.size)
        check(hash.size == path.size)
        check(hash.size == fade.size)

        val pathB = path.toMutableList().apply { this[idx] = 1 }

        if (idx == hash.size - 1) {
            val hashA = path.foldIndexed(0) { i, acc, it ->
                perlinPermutation(acc + hash[i] + it)
            }
            val valuesA = values.zip(path) { v, p -> v - p }
            val gradA = gradient(hashA, valuesA, outputSize)

            val hashB = pathB.foldIndexed(0) { i, acc, it ->
                perlinPermutation(acc + hash[i] + it)
            }
            val valuesB = values.zip(pathB) { v, p -> v - p }
            val gradB = gradient(hashB, valuesB, outputSize)

            return lerp(gradA, gradB, fade[idx])
        } else {
            val a = interpolate(idx + 1, hash, values, path, fade, outputSize)
            val b = interpolate(idx + 1, hash, values, pathB, fade, outputSize)

            return lerp(a, b, fade[idx])
        }
    }

    private fun lerp(aList: List<Double>, bList: List<Double>, t: Double): List<Double> {
        check(aList.size == bList.size)

        return aList.zip(bList) { a, b ->
            interpolation.lerp(a, b, t)
        }
    }

    private fun gradient(hash: Int, values: List<Double>, outputSize: Int): List<Double> {
        return DoubleArray(outputSize) {
            (gradient(hash + it, values) + 1.0) / 2.0
        }.toList()
    }

    private fun gradient(hash: Int, input: List<Double>): Double {
        var sum = 0.0
        var h = hash
        input.forEachIndexed { _, d ->
            when (h % 3) {
                0 -> sum += d
                1 -> sum -= d
            }
            h = (h - (h % 3)) / 3
        }
        return sum / input.size
    }

    private fun perlinHash(d: Double): Int {
        val index = floor(d).toInt()
        return if (index < 0) {
            (index % permutations.size) + permutations.size
        } else {
            (index % permutations.size)
        }
    }

    private fun perlinPermutation(index: Int): Int {
        val i = if (index < 0) {
            (index % permutations.size) + permutations.size
        } else {
            (index % permutations.size)
        }
        return permutations[i]
    }

    // endregion

    companion object {

        var maxSum = 0.0

        // Allows 8 hash permutations
        private val permutations = intArrayOf(
            393, 482, 418, 59, 145, 7, 387, 279, 312, 153, 196, 483, 122, 489, 438, 448,
            6, 240, 481, 212, 3, 443, 318, 124, 174, 190, 353, 149, 344, 331, 407, 338,
            163, 191, 361, 348, 460, 376, 73, 426, 334, 300, 242, 381, 208, 184, 175, 420,
            303, 98, 187, 455, 330, 413, 111, 477, 467, 197, 468, 193, 49, 218, 307, 38,
            464, 411, 435, 408, 294, 126, 134, 23, 76, 225, 50, 35, 110, 150, 10, 139,
            77, 231, 79, 83, 373, 66, 8, 188, 248, 447, 386, 238, 209, 333, 342, 335,
            55, 392, 478, 276, 459, 332, 14, 230, 440, 427, 207, 151, 325, 314, 267, 382,
            508, 262, 67, 365, 64, 34, 461, 414, 204, 164, 246, 355, 44, 302, 449, 82,
            491, 454, 29, 65, 135, 310, 316, 423, 324, 360, 352, 155, 309, 298, 346, 16,
            274, 54, 366, 425, 232, 345, 104, 269, 488, 94, 273, 179, 380, 445, 289, 52,
            474, 268, 244, 463, 347, 398, 87, 47, 69, 465, 399, 234, 286, 56, 271, 89,
            409, 337, 75, 495, 233, 186, 13, 206, 144, 266, 213, 450, 215, 329, 328, 18,
            117, 241, 428, 421, 506, 484, 480, 479, 182, 48, 259, 84, 239, 203, 326, 137,
            415, 120, 510, 123, 362, 442, 181, 446, 404, 12, 343, 403, 433, 472, 131, 136,
            263, 194, 236, 195, 178, 255, 26, 357, 397, 458, 180, 20, 127, 282, 4, 33,
            214, 469, 99, 114, 161, 297, 323, 368, 494, 498, 216, 320, 487, 237, 221, 351,
            19, 321, 168, 147, 157, 492, 90, 281, 63, 30, 264, 295, 185, 108, 291, 293,
            217, 11, 143, 367, 401, 370, 205, 462, 410, 17, 490, 129, 220, 86, 284, 130,
            444, 354, 253, 171, 2, 395, 41, 315, 364, 53, 358, 275, 272, 359, 210, 156,
            493, 299, 31, 304, 363, 283, 235, 257, 60, 103, 118, 201, 97, 260, 349, 339,
            169, 121, 72, 251, 142, 379, 319, 177, 21, 162, 229, 166, 228, 485, 40, 219,
            70, 106, 101, 43, 412, 406, 211, 305, 100, 32, 437, 119, 24, 471, 375, 158,
            311, 254, 290, 200, 405, 252, 81, 154, 277, 93, 0, 5, 509, 74, 497, 9,
            322, 68, 389, 36, 107, 37, 61, 296, 327, 28, 429, 383, 378, 511, 183, 198,
            502, 394, 424, 503, 148, 189, 308, 280, 496, 25, 500, 417, 138, 385, 85, 336,
            116, 141, 172, 128, 92, 249, 466, 247, 432, 400, 78, 173, 109, 419, 473, 176,
            223, 45, 292, 340, 457, 39, 91, 451, 105, 250, 265, 350, 422, 388, 270, 384,
            499, 288, 402, 42, 287, 102, 441, 222, 113, 46, 27, 165, 313, 51, 341, 88,
            15, 285, 243, 371, 160, 369, 202, 486, 226, 258, 245, 57, 227, 58, 301, 95,
            507, 377, 501, 504, 71, 374, 452, 317, 133, 356, 170, 22, 436, 470, 199, 396,
            159, 112, 390, 391, 1, 505, 475, 306, 140, 80, 125, 96, 224, 416, 256, 132,
            453, 152, 192, 431, 430, 456, 476, 278, 146, 434, 115, 439, 261, 167, 62, 372,
        )
    }
}

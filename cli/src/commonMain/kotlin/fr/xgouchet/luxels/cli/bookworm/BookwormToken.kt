package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Vector1
import fr.xgouchet.luxels.core.math.Vector2
import fr.xgouchet.luxels.core.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

data class BookwormToken(
    val value: String, // lorem
    val initialPosInSentence: Int, // 0
) {

    // Normalized position in dictionary from A = 0.0 to Zzzzzzzzzzzzzzz… = 1.0
    val alphaRank by lazy {
        var temp = 0.0
        value.forEachIndexed { index, c ->
            val charRank = if (c in 'a'..'z') {
                ((c - 'a').toDouble() + 1.0) / RANK_FACTOR
            } else {
                TODO()
            }
            temp += (charRank / 10.0.pow(index))
        }
        temp
    }

    val scrabbleRank by lazy {
        value.map { SCRABBLE[it] ?: 0.0 }.sum()
    }

    val lengthRank = value.length / MAX_WORD_SIZE

    var sortedIndex = 0.0

    init {
        if (scrabbleRank > MAX_SCRABBLE_POINTS) println("$value -> $scrabbleRank")
    }

    fun getPosition(): Vector<Dimension.D3> {
        return Vector3(
            x = alphaRank,
            y = (scrabbleRank / MAX_SCRABBLE_POINTS),
            z = lengthRank
            // lengthRank
            // initialPosInSentence / MAX_SENTENCE_SIZE
        )
    }

    fun getIntensity(): Vector<Dimension.D2> {
        val y = (initialPosInSentence.mod(2) * 2.0) - 1.0
        return if (value in KEYWORDS) {
            Vector2(2.5, y)
        } else {
            Vector2(0.5, y)
        }
    }

    override fun toString(): String {
        return value
    }

    companion object {
        const val MAX_SENTENCE_SIZE = 64.0
        const val MAX_WORD_SIZE = 16.0
        const val MAX_SCRABBLE_POINTS = 1000.0

        const val RANK_FACTOR = 32.0

        private var SCRABBLE = mapOf(
            'a' to 1.0 / 0.0747,
            'b' to 1.0 / 0.0114,
            'c' to 1.0 / 0.0324,
            'd' to 1.0 / 0.0367,
            'e' to 1.0 / 0.1444,
            'f' to 1.0 / 0.0111,
            'g' to 1.0 / 0.0123,
            'h' to 1.0 / 0.0111,
            'i' to 1.0 / 0.0664,
            'j' to 1.0 / 0.0034,
            'k' to 1.0 / 0.0029,
            'l' to 1.0 / 0.0496,
            'm' to 1.0 / 0.0262,
            'n' to 1.0 / 0.0639,
            'o' to 1.0 / 0.0507,
            'p' to 1.0 / 0.0249,
            'q' to 1.0 / 0.0065,
            'r' to 1.0 / 0.0607,
            's' to 1.0 / 0.0651,
            't' to 1.0 / 0.0592,
            'u' to 1.0 / 0.0454,
            'v' to 1.0 / 0.0111,
            'w' to 1.0 / 0.0017,
            'x' to 1.0 / 0.0038,
            'y' to 1.0 / 0.0046,
            'z' to 1.0 / 0.0025,
        )

        val KEYWORDS = arrayOf(
            "capitaine", "nemo", "nautilus", "monstre", "ned", "land", "conseil", "aronnax", "pacifique", "atlantide",
            "poulpe", "calmar", "scotia", "chaperon", "rouge", "grandmere", "loup", "chasseur"
        )
    }
}
package fr.xgouchet.luxels.core.gen.noise

import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Provides a noise based on the concept behind Voronoi diagram.
 *
 * A virtual grid is used to pave the output space, and in each cell a Voronoi center
 * is created, and used to generate a value based on the distance between each point and the
 * nearest Voronoi center.
 */
class VoronoiNoiseGenerator : DimensionalNoiseGenerator {

    private val random = Random(0L)

    // region DimensionalNoise

    override fun noise(input: List<Double>, outputSize: Int): List<Double> {
        val floor = input.map { floor(it).toInt() }
        val frac = input.map { it - floor(it) }

        val scale = sqrt(input.size.toDouble())
        return (0..<outputSize).map {
            distanceToClosestCenter(floor, frac, it) / scale
        }
    }

    // endregion

    // region Internal

    private fun distanceToClosestCenter(tile: List<Int>, positionInTile: List<Double>, hash: Int): Double {
        val path = IntArray(tile.size) { 0 }.toList()
        return distanceToClosestCenter(
            tile,
            positionInTile,
            path,
            hash,
            0,
        )
    }

    private fun distanceToClosestCenter(
        tile: List<Int>,
        positionInTile: List<Double>,
        path: List<Int>,
        hash: Int,
        varyingDimension: Int,
    ): Double {
        val prevTilePath = path.toMutableList().apply { this[varyingDimension] -= 1 }
        val nextTilePath = path.toMutableList().apply { this[varyingDimension] += 1 }

        return if (varyingDimension == (tile.size - 1)) {
            val prevCenter = centerPosition(tile, prevTilePath, hash)
            val thisCenter = centerPosition(tile, path, hash)
            val nextCenter = centerPosition(tile, nextTilePath, hash)
            val prevDistance = distance(prevCenter, positionInTile)
            val thisDistance = distance(thisCenter, positionInTile)
            val nextDistance = distance(nextCenter, positionInTile)
            min(min(prevDistance, thisDistance), nextDistance)
        } else {
            val prevDistance = distanceToClosestCenter(tile, positionInTile, prevTilePath, hash, varyingDimension + 1)
            val thisDistance = distanceToClosestCenter(tile, positionInTile, path, hash, varyingDimension + 1)
            val nextDistance = distanceToClosestCenter(tile, positionInTile, nextTilePath, hash, varyingDimension + 1)
            min(min(prevDistance, thisDistance), nextDistance)
        }
    }

    private fun distance(aList: List<Double>, bList: List<Double>): Double {
        val sum = aList.zip(bList) { a, b -> (a - b) * (a - b) }.sum()
        return sqrt(sum)
    }

    private fun centerPosition(tile: List<Int>, path: List<Int>, hash: Int): List<Double> {
        val seed = tile.foldIndexed(0L) { i, acc, it ->
            (acc * primeFactors[i]) + (it + path[i])
        }
        val rnd = Random(seed * primeFactors[hash])
        return List(tile.size) { i ->
            rnd.nextDouble() + path[i]
        }
    }

    // endregion

    companion object {

        private val primeFactors = intArrayOf(
            1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061,
            1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123,
            1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213,
            1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283,
            1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361,
            1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439,
            1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493,
            1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571,
            1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627,
            1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721,
            1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789,
            1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877,
            1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973,
            1979, 1987, 1993, 1997, 1999,
        )
    }
}

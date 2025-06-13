package art.luxels.imageio.format.hdr

/**
 * A data class used to store a representation of a positive decimal number.
 * Similarly to the IEEE Double representation, it's value is equal to
 *
 *     fraction ⨉ 2ᵉˣᵖ
 *
 */
internal data class HdrDouble(val fraction: Double, val exponent: Int) {
    companion object {
        /**
         * An IEEE double uses 64 bits, as follow :
         *  - 52 bits for the fraction f
         *  - 11 bits for the exponent e
         *  - 1 bit for the sign
         * If the exponent is non null, the unsigned value is then 2^e−1023 × ( 1 + ∑ bi 2^-i )
         * It the exponent is null, the unsigned value is 2^-1022 × ( ∑ bi 2^-i )
         *  More info https://bartaz.github.io/ieee754-visualization/
         */
        fun fromDouble(d: Double): HdrDouble {
            if (d == 0.0 || d == -0.0) {
                return HdrDouble(d, 0)
            }
            require(d.isFinite())

            var x = d
            var bits = x.toRawBits()
            var bitsMantissa = bits and MASK_MANTISSA
            var bitsExponent = bits and MASK_EXPONENT

            var exponent = 0

            val isSubnormal = bitsExponent == 0L

            // Shift the double up to have an exponent of 54
            if (isSubnormal) {
                x *= DOUBLE_EXP_54
                bits = x.toRawBits()
                bitsMantissa = bits and MASK_MANTISSA
                bitsExponent = bits and MASK_EXPONENT
                exponent = EXP_MINUS_54
            }

            // compute the final exponent offset by 1022
            val rawExponent = (bitsExponent shr 52).toInt()
            exponent += (rawExponent - EXPONENT_1022)

            // the fraction is then the mantissa, with a fixed exponent of -1 (= 1022 - 1023)
            val newBits = bitsMantissa or BITS_EXPONENT_1022

            return HdrDouble(Double.fromBits(newBits), exponent)
        }

        private const val MASK_INT = 0xFFFFFFFFL
        private const val MASK_UNSIGNED_INT = 0x7FFFFFFF

        private const val MASK_EXPONENT = 0x7FF0000000000000L
        private const val MASK_MANTISSA = 0x000FFFFFFFFFFFFFL
        private const val MASK_SIGNED_MANTISSA_HIGH = 0x800FFFFFL

        private const val HIGH_BITS_EXPONENT_1022 = 0x3FE00000L
        private const val BITS_EXPONENT_1022 = 0x3FE0000000000000L
        private const val EXPONENT_1022 = 0x3FE

        /**
         * Represents a positive double with the exponent bits set to 1077,
         * resolved as an exponent of 54 (1077 - 1023 = 54).
         */
        private const val DOUBLE_EXP_54 = 1.8014398509481984E16
        private const val EXP_MINUS_54 = -54
    }
}

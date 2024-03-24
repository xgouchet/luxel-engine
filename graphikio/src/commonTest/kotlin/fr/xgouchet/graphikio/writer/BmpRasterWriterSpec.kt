package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.format.bmp.BmpImageFormat
import fr.xgouchet.graphikio.format.bmp.BmpRasterWriter
import io.kotest.core.spec.style.DescribeSpec

class BmpRasterWriterSpec : DescribeSpec({

    include(AbstractRasterWriterSpec(BmpImageFormat) { BmpRasterWriter() })
})

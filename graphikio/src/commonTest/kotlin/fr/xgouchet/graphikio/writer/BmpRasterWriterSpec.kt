package fr.xgouchet.graphikio.writer

import io.kotest.core.spec.style.DescribeSpec

class BmpRasterWriterSpec : DescribeSpec({

    include(AbstractRasterWriterSpec { BmpRasterWriter() })
})

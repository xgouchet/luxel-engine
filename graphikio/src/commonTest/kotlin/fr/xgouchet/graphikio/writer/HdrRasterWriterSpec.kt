package fr.xgouchet.graphikio.writer

import fr.xgouchet.graphikio.format.hdr.HdrImageFormat
import fr.xgouchet.graphikio.format.hdr.HdrRasterWriter
import io.kotest.core.spec.style.DescribeSpec

class HdrRasterWriterSpec : DescribeSpec({

    include(AbstractRasterWriterSpec(HdrImageFormat) { HdrRasterWriter() })
})

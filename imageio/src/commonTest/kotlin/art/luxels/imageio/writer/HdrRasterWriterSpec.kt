package art.luxels.imageio.writer

import art.luxels.imageio.format.hdr.HdrImageFormat
import art.luxels.imageio.format.hdr.HdrRasterWriter
import io.kotest.core.spec.style.DescribeSpec

class HdrRasterWriterSpec : DescribeSpec({

    include(abstractRasterWriterSpec(HdrImageFormat) { HdrRasterWriter() })
})

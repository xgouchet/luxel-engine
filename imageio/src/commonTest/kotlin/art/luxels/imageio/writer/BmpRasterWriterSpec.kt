package art.luxels.imageio.writer

import art.luxels.imageio.format.bmp.BmpImageFormat
import art.luxels.imageio.format.bmp.BmpRasterWriter
import io.kotest.core.spec.style.DescribeSpec

class BmpRasterWriterSpec : DescribeSpec({

    include(abstractRasterWriterSpec(BmpImageFormat) { BmpRasterWriter() })
})

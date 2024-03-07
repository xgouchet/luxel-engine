package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.JvmHdrImageFixer

actual val mainFixer: ImageFixer = JvmHdrImageFixer()

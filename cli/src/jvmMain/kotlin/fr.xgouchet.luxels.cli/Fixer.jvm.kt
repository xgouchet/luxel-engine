package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.core.io.HdrImageFixer
import fr.xgouchet.luxels.core.io.ImageFixer

actual val mainFixer: ImageFixer = HdrImageFixer()

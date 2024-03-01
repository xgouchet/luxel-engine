package fr.xgouchet.luxels.cli

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer

actual val mainFixer: ImageFixer = NoOpFixer()

package fr.xgouchet.graphikio

import okio.FileSystem

/** Shortcut to the underlying Okio Filesystem. */
actual val fileSystem: FileSystem = FileSystem.SYSTEM

package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.graphikio.fileSystem
import fr.xgouchet.luxels.core.log.LogHandlerExt
import okio.Path
import okio.buffer
import okio.use

// TODO pre process input text into a precomputed curves format ?
internal class BookwormInputReader {

    // region BookwormInputReader

    // TODO split and refactor this method
    @Suppress("CyclomaticComplexMethod", "CognitiveComplexMethod", "NestedBlockDepth")
    fun getInput(path: Path, logger: LogHandlerExt): BookwormInput {
        var longestSentenceSize = 0

        // step 1 : read the file
        val lines = readLines(path)

        // step 2
        val sentences = mutableListOf<BookwormSentence>()
        // TODO assumption: a sentence is not spread across multiple lines

        @Suppress("DoubleMutabilityForCollection")
        var ongoingSentence = mutableListOf<BookwormToken>()
        var ongoingToken = ""

        lines.forEach { line ->
            line.forEach { char ->

                when (char) {
                    in 'a'..'z' -> ongoingToken += char
                    'à', 'â', 'ä' -> ongoingToken += 'a'
                    'é', 'è', 'ê', 'ë' -> ongoingToken += 'e'
                    'î', 'ï' -> ongoingToken += 'i'
                    'ô', 'ö' -> ongoingToken += 'o'
                    'û', 'ü', 'ù' -> ongoingToken += 'u'
                    'ÿ' -> ongoingToken += 'y'
                    'ç' -> ongoingToken += 'c'
                    'œ' -> ongoingToken += "oe"
                    'æ' -> ongoingToken += "ae"

                    in midTokenChars -> {
                        // no op
                    }

                    in '0'..'9' -> {
                        // no op
                    }

                    in endOfTokenChars -> {
                        if (ongoingToken.length > 3 || ongoingToken == "ned") {
                            ongoingSentence.add(BookwormToken(ongoingToken, ongoingSentence.size))
                        }
                        ongoingToken = ""
                    }

                    in endOfSentenceChars -> {
                        if (ongoingToken.length > 3 || ongoingToken == "ned") {
                            ongoingSentence.add(BookwormToken(ongoingToken, ongoingSentence.size))
                        }
                        ongoingToken = ""
                        if (ongoingSentence.size > 2) {
                            sentences.add(BookwormSentence(ongoingSentence, sentences.size))
                            if (ongoingSentence.size > longestSentenceSize) {
                                longestSentenceSize = ongoingSentence.size
                            }
                        }
                        ongoingSentence = mutableListOf()
                    }

                    else -> {
                        missingChars[char] = missingChars.getOrElse(char) { 0 } + 1
                    }
                }
            }
        }

        logger.debug("Longest sentences has $longestSentenceSize tokens")
        logger.debug("Total sentences count: ${sentences.size}")
        return BookwormInput(sentences)
    }

    // endregion

    // region Internal

    @Suppress("NestedBlockDepth")
    private fun readLines(path: Path): List<String> {
        val lines = mutableListOf<String>()

        // TODO make sure everything is closed and memory freed !!!!!
        fileSystem.source(path).use { fileSource ->
            fileSource.buffer().use { buffer ->
                var isEof = false
                while (!isEof) {
                    val line = buffer.readUtf8Line()
                    if (line == null) {
                        isEof = true
                    } else if (line.isNotBlank()) {
                        lines.add(line.lowercase())
                    }
                }
            }
        }

        return lines
    }

    // endregion

    companion object {
        val endOfSentenceChars = charArrayOf('.', '!', '?', '…')
        val endOfTokenChars = charArrayOf(
            ' ', '', '\n', '\t', '’', '\'', ',', ':',
            ';', '(', ')', '«', '»', '–', '—', '“', '”',
        )
        val midTokenChars = charArrayOf('-')

        val missingChars = mutableMapOf<Char, Int>()
    }
}

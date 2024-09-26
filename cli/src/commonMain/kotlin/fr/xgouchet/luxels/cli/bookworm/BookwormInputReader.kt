package fr.xgouchet.luxels.cli.bookworm

import fr.xgouchet.graphikio.fileSystem
import fr.xgouchet.luxels.core.log.Logger
import okio.Path
import okio.buffer
import okio.use

// TODO pre process input text into a precomputed curves format
class BookwormInputReader {

    fun getInput(path: Path, logger: Logger): BookwormInput {
        var longestSentenceSize = 0

        // step 1 : read the file
        val lines = readLines(path)

        // step 2
        val sentences = mutableListOf<BookwormSentence>()
        // TODO assumption: a sentence is not spread across multiple lines

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

    private fun readLines(path: Path): List<String> {
        val lines = mutableListOf<String>()

        // TODO make sure everything is closed and memory freed !!!!!
        fileSystem.source(path).use { fileSource ->
            fileSource.buffer().use { buffer ->
                var eof = false
                while (!eof) {
                    val line = buffer.readUtf8Line()
                    if (line == null) {
                        eof = true
                    } else if (line.isNotBlank()) {
                        lines.add(line.lowercase())
                    }
                }
            }
        }


        return lines
    }

    companion object {
        val endOfSentenceChars = arrayOf('.', '!', '?', '…')
        val endOfTokenChars = arrayOf(' ', '', '\n', '\t', '’', '\'', ',', ':', ';', '(', ')', '«', '»', '–', '—', '“', '”')
        val midTokenChars = arrayOf('-')

        val missingChars = mutableMapOf<Char, Int>()
    }
}
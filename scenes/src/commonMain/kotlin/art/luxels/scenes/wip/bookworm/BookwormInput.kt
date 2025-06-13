package art.luxels.scenes.wip.bookworm

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BookwormInput(private val sentences: List<BookwormSentence>) {
    private var cursor: Int = 0

    private val mutex = Mutex()

    suspend fun nextSentence(): BookwormSentence {
        return mutex.withLock {
            val sentence = sentences[cursor]
            cursor = (cursor + 1).mod(sentences.size)

            sentence
        }
    }

    fun getSentenceCount(): Int {
        return sentences.size
    }
}

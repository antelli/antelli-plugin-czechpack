package io.antelli.plugin.seznam.search

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.RemoteException

import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 25.06.2018.
 */
class SeznamSearchService : AntelliPlugin() {

    private val keywords = arrayOf("hledej", "najdi", "hledat", "na seznamu")

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne(*keywords))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        val answer = Answer()
        val subject = question.removeWords(*keywords)
        answer.setAutoRun(Intent(Intent.ACTION_VIEW, Uri.parse("https://search.seznam.cz/?q=$subject")))
        callback.answer(answer)
    }

    @Throws(RemoteException::class)
    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return null
    }
}

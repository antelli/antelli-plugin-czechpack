package io.antelli.plugin.heureka

import android.content.Intent
import android.net.Uri
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.reactivex.Observable

class HeurekaPlugin : AntelliPlugin() {
    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        val query = question.removeWords("na heurece", "heureka", "hledat", "hledej", "najdi", "kolik stojí", "kolik stojej")
        callback.answer(Answer().apply {
            autoRun = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.heureka.cz/?h%5Bfraze%5D="+query.replace(" ", "+")))
            addItem(AnswerItem().apply {
                text = "Hledám $query na Heurece"
                speech = "Hledám $query na Heurece"
            })
        })
    }

    override fun reset() {

    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("kolik stojí", "kolik stojej", "heuréka", "heuréce"))
    }

}
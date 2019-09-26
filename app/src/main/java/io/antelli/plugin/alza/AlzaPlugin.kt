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

class AlzaPlugin : AntelliPlugin() {
    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        val query = question.removeWords("na alze", "na alza", "alza", "hledat", "hledej", "najdi", "najít", "za kolik je", "za kolik jsou", "kolik stojí", "kolik stojej")
        callback.answer(Answer().setAutoRun(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alza.cz/search.htm?IDP=10091&exps="+query)))
                .addItem(AnswerItem().setText("Hledám $query na Alze").setSpeech("Hledám $query na Alze")))
    }

    override fun reset() {

    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("kolik stojí", "kolik stojej", "alza", "alze", "za kolik"))
    }

}
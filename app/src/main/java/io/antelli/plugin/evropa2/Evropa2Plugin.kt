package io.antelli.plugin.evropa2

import android.content.Intent
import android.net.Uri
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

class Evropa2Plugin : AntelliPlugin() {
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("evropa 2", "evropu 2"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {

        callback.answer(Answer().apply {
            addItem(AnswerItem().apply {
                type = AnswerItem.TYPE_AUDIO
                title = "Evropa 2"
                speech = "Zapínám Evropu 2"
                stream = "http://ice.actve.net/fm-evropa2-128"
            })
        })
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }
}
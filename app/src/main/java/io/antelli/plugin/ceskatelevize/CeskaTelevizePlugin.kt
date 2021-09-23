package io.antelli.plugin.ceskatelevize

import android.content.Intent
import android.net.Uri
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

class CeskaTelevizePlugin : AntelliPlugin() {
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("čt1", "čt2", "čt24", "čt sport", "déčko", "čt art"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {

        var url : String? = null
        var text : String? = null

        if (question.containsWord("sport")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct4"
            text = "Spouštím živé vysílání ČT Sport."
        } else if (question.containsWord("čt1")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct1"
            text = "Spouštím živé vysílání ČT1."
        } else if (question.containsWord("čt2")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct2"
            text = "Spouštím živé vysílání ČT2."
        } else if (question.containsWord("čt24")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct3"
            text = "Spouštím živé vysílání ČT24."
        } else if (question.containsWord("déčko")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct5"
            text = "Spouštím živé vysílání ČT:D."
        } else if (question.containsWord("art")){
            url = "https://www.ceskatelevize.cz/ivysilani/zive/ct6"
            text = "Spouštím živé vysílání ČT Art."
        }

        callback.answer(Answer(text).apply {
            autoRun = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        })
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }
}
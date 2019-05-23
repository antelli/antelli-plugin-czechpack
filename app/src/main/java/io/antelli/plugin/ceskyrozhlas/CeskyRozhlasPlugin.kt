package io.antelli.plugin.ceskyrozhlas

import android.content.Intent
import android.net.Uri
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

class CeskyRozhlasPlugin : AntelliPlugin() {
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("rozhlas", "radiožurnál"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {

        var url: String?
        var text: String?

        if (question.containsOne("dvojka")){
            url = "http://icecast6.play.cz/cro2-128.mp3"
            text = "Český rozhlas Dvojka"
        } else if (question.containsOne("vltava")){
            url = "http://icecast5.play.cz/cro3-128.mp3"
            text = "Český rozhlas Vltava"
        } else if (question.containsOne("plus")){
            url = "http://icecast1.play.cz/croplus128.mp3"
            text = "Český rozhlas Plus"
        }else if (question.containsOne("wave")){
            url = "http://icecast6.play.cz/crowave-128.mp3"
            text = "Český rozhlas Wave"
        }else if (question.containsOne("dur")){
            url = "http://icecast5.play.cz/croddur-128.mp3"
            text = "Český rozhlas D dur"
        }else if (question.containsOne("jazz")){
            url = "http://icecast1.play.cz/crojazz128.mp3"
            text = "Český rozhlas Jazz"
        }else if (question.containsOne("junior")){
            url = "http://icecast5.play.cz/crojuniormaxi128.mp3"
            text = "Český rozhlas Junior"
        }else if (question.containsOne("retro")){
            url = "http://icecast7.play.cz/croretro128.mp3"
            text = "Český rozhlas Retro"
        }else {
            url = "http://icecast8.play.cz/cro1-128.mp3"
            text = "Český rozhlas Radiožurnál"
        }

        callback.answer(Answer().addItem(AnswerItem().setType(AnswerItem.TYPE_AUDIO).setTitle(text).setSpeech("Zapínám $text").setStream(url)))
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }
}
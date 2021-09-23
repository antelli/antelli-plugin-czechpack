package io.antelli.plugin.inpocasi

import android.util.Log
import io.antelli.plugin.BaseApiPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InPocasiPlugin : BaseApiPlugin<InPocasiRepository>() {

    override fun answer(question: Question, callback: IAnswerCallback) {
        GlobalScope.launch {
            runCatching {
                api.getWeather()
            }.onSuccess {
                callback.answer(Answer().apply {
                    addItem(AnswerItem().apply {
                        type = AnswerItem.TYPE_CARD
                        text = it.weatherForecast.first().text
                        speech = it.weatherForecast.first().text
                        image = "https://www.in-pocasi.cz/media/images/weather/${it.weatherForecast.first().state}.svg"
                    })
                })
                Log.d("ok", "ok")
            }.onFailure {
                it.printStackTrace()
            }

        }
    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("počasí", "předpověď", "jak bude", "jak má být", "jak má bejt"))
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }

    override fun initApi(): InPocasiRepository {
        return InPocasiRepository()
    }
}
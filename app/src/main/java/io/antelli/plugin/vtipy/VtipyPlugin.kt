package io.antelli.plugin.vtipy

import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.vtipy.entity.Joke
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.*

class VtipyPlugin : BaseWebPlugin<VtipyApi>() {
    override fun initApi(): VtipyApi {
        return VtipyApi()
    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("vtip"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        api.getJoke()
                .map { response -> convert(response) }
                .subscribe(
                        { answer -> callback.answer(answer) },
                        { e -> callback.answer(ErrorAnswer()) }
                )
    }

    private fun convert(item: Joke): Answer {
        return Answer(item.text)
    }

}

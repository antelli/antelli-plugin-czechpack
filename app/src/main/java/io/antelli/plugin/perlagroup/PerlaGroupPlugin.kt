package io.antelli.plugin.perlagroup

import android.app.Activity
import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.perlagroup.entity.Story
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

class PerlaGroupPlugin : BaseWebPlugin<PerlaGroupApi>() {

    override fun initApi(): PerlaGroupApi {
        return PerlaGroupApi()
    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("pohádka", "pohádku", "pohádky"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        api.getStories().subscribe(
                { callback.answer(convert(it)) },
                {
                    it.printStackTrace()
                    callback.answer(Answer("Něco se pokazilo, zkus to později a případně kontaktuj vývojáře"))
                }
        )
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }

    private fun convert(list: List<Story>): Answer {
        val result = Answer()
        for (story in list) {

            result.addItem(
                    AnswerItem().apply {
                        type = AnswerItem.TYPE_AUDIO
                        speech = story.name
                        stream = story.url
                    })
        }
        return result
    }
}
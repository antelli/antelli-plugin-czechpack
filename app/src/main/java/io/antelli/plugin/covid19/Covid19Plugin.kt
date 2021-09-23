package io.antelli.plugin.covid19

import io.antelli.plugin.BaseWebPlugin
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

class Covid19Plugin : BaseWebPlugin<Covid19Api>() {
    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        api.getData().subscribe({
            when{
                question.contains("test") -> callback.answer(Answer("Celkem bylo provedeno ${it.tests} testů."))
                question.contains("nakažen") -> callback.answer(Answer("Aktuálně máme ${it.infected - it.recovered - it.dead} nakažených"))
                question.contains("uzdrav") -> callback.answer(Answer("Z koronaviru se už uzdravilo ${it.recovered} lidí."))
                question.contains("mrtv") -> callback.answer(Answer("Na koronavirus zemřelo už ${it.dead} lidí."))
                else -> callback.answer(Answer("Aktuálně máme ${it.infected - it.recovered - it.dead} nakažených, celkem onemocnělo ${it.infected} lidí, z toho se ${it.recovered} uzdravilo a ${it.dead} zemřelo. Bylo provedeno ${it.tests} testů."))
            }
        },{
            callback.answer(Answer("Nepodařilo se zjistit aktuální data o koronaviru, pokud problém přetrvává, kontaktujte vývojáře."))
        })
    }

    override fun reset() {

    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        return callback.canAnswer(question.containsOne("koronavir", "coronavir", "nakažen", "mrtvých", "zemřelo", "uzdrav", "testovan", "testován"))
    }

    override fun initApi(): Covid19Api {
        return Covid19Api()
    }
}
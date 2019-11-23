package io.antelli.plugin.svatky

import io.antelli.plugin.base.util.DateTimeCortex
import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class SvatkyPlugin : AntelliPlugin() {

    val repo = SvatkyRepo()
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        if (question.contains("kdy")){
            var name = question.removeWords("kdy", "má", "svátek", "bude", "mít")
            name = name.capitalize()

            GlobalScope.launch {
                val result = repo.getNameday(name)
                if(result.size == 0){
                    callback.answer(Answer("Jméno $name jsem v českém kalendáři nenašla"))
                } else {
                    val sb = StringBuilder("$name má svátek ")
                    val dateFormat = SimpleDateFormat("d. MMMM", Locale("cs", "CZ"))
                    for (i in result.indices){
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.DAY_OF_MONTH, result[i].den.toInt())
                        cal.set(Calendar.MONTH, result[i].mesic.toInt()-1)
                        sb.append(dateFormat.format(cal.time))
                        if (i < result.size - 1) {
                            sb.append(" a ")
                        }
                    }
                    callback.answer(Answer(sb.toString()))
                }
            }
        } else {

            val cal = DateTimeCortex.getDateTimeFromString(question)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH) + 1

            GlobalScope.launch {
                val result = repo.getNameday(day, month)

                val sb = StringBuilder()
                for (i in result.indices) {
                    sb.append(result[i].jmeno)
                    if (i < result.size - 1) {
                        sb.append(" a ")
                    }
                }
                callback.answer(Answer(sb.toString()))
            }
        }
    }

    override fun reset() {

    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("svátek"))
    }
}
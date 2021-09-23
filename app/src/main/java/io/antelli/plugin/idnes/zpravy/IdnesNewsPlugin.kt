package io.antelli.plugin.idnes.zpravy

import android.os.RemoteException
import io.antelli.plugin.BaseApiPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 10.11.2017.
 */

class IdnesNewsPlugin : BaseApiPlugin<IdnesApiClient>() {

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("zprávy", "novinky", "co nového"))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        api.getArticles(question).subscribe({ answer ->
            callback.answer(answer)
        }, {
            callback.answer(ErrorAnswer())
        })
    }

    @Throws(RemoteException::class)
    override fun command(command: Command, callback: IAnswerCallback) {

        val type = command.action

        when (type) {
            ACTION_ARTICLE -> api.getArticle(command.getString(PARAM_ID) ?: "").subscribe({ answer ->
                callback.answer(answer)
            }, {
                callback.answer(ErrorAnswer())
            })
            ACTION_SECTION -> api.getArticles(command.getString(PARAM_ID)).subscribe({ answer ->
                callback.answer(answer)
            }, {
                callback.answer(ErrorAnswer())
            })
        }
    }

    override fun reset() {

    }

    override fun initApi(): IdnesApiClient {
        return IdnesApiClient()
    }

    companion object {

        val PARAM_ID = "id"
        val ACTION_SECTION = "selection"
        val ACTION_ARTICLE = "article"
    }
}

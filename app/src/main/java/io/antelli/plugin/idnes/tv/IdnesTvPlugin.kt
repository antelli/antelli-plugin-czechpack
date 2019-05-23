package io.antelli.plugin.idnes.tv

import android.app.Activity
import android.os.RemoteException

import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 21.11.2017.
 */

class IdnesTvPlugin : BaseWebPlugin<IdnesTvApi>() {


    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("televizní program", "program televize", "co dávají", "co dávaj", "co budou dávat"))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        api.answer(question).subscribe({ answer -> callback.answer(answer) }, { callback.answer(ErrorAnswer()) })
    }

    override fun initApi(): IdnesTvApi {
        return IdnesTvApi()
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return IdnesTvSettingsActivity::class.java
    }
}

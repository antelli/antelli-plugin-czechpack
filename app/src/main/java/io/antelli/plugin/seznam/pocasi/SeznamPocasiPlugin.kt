package io.antelli.plugin.seznam.pocasi

import android.app.Activity
import android.os.RemoteException
import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

class SeznamPocasiPlugin : BaseWebPlugin<SeznamPocasiApi>() {

    override fun initApi(): SeznamPocasiApi {
        return SeznamPocasiApi(this)
    }

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("počasí", "předpověď", "jak bude", "jak má být", "jak má bejt"))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        api.answer(question).subscribe({ answer -> callback.answer(answer) }, { callback.answer(ErrorAnswer()) })
    }

    override fun command(command: Command, callback: IAnswerCallback) {
        api.command(command)
                .subscribe({ answer -> callback.answer(answer) },
                        { throwable -> callback.answer(ErrorAnswer()) })
    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return SeznamPocasiSettingsActivity::class.java
    }
}

package io.antelli.plugin

import io.antelli.sdk.AntelliPlugin
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.model.Command

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

abstract class BaseWebPlugin<T : BaseWebApi> : AntelliPlugin() {

    protected var api: T

    init {
        api = this.initApi()
    }

    protected abstract fun initApi(): T

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {

    }
}

package io.antelli.plugin

import io.antelli.sdk.AntelliPlugin

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

abstract class BaseApiPlugin<T : BaseApi<*>> : AntelliPlugin() {

    protected var api: T

    init {
        api = this.initApi()
    }

    protected abstract fun initApi(): T
}

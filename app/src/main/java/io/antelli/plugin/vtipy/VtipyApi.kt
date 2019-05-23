package io.antelli.plugin.vtipy

import io.antelli.plugin.BaseWebApi
import io.reactivex.Observable
import io.antelli.plugin.vtipy.entity.Joke


class VtipyApi : BaseWebApi() {

    fun getJoke() : Observable<Joke> {
        val link = "https://www.vtipy.net/hodnotit-vtipy"

        return getHtmlFrom(link).map {
            var parsed = parse(it, "</script></div>(.*?)</div>")
            return@map Joke(removeHtmlTags(parsed.first(0)))
        }
    }
}
package io.antelli.plugin.covid19

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.covid19.entity.Covid19Entity
import io.reactivex.Observable

class Covid19Api : BaseWebApi() {

    fun getData(): Observable<Covid19Entity> {
        return getHtmlFrom("https://onemocneni-aktualne.mzcr.cz/covid-19")
                .map {
                    val parsed = parse(it, "count-test.*?>(.*?)<.*?count-sick.*?>(.*?)<.*?count-recover.*?>(.*?)<.*?count-dead.*?>(.*?)<")
                    return@map Covid19Entity(parsed.first(0).replace(" ", ""), parsed.first(1).replace(" ", ""), parsed.first(2).replace(" ", ""), parsed.first(3).replace(" ", ""))
                }
    }
}
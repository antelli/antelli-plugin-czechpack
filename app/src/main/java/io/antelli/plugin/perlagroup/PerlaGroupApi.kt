package io.antelli.plugin.perlagroup

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.perlagroup.entity.Story
import io.reactivex.Observable

class PerlaGroupApi : BaseWebApi() {

    fun getStories() : Observable<List<Story>>{
        return getHtmlFrom("https://perlagroup.eu/?3852")
            .map {
                val parsed = parse(it.substring(it.indexOf("<div class=\"misto3 clear\">"), it.indexOf("<div id=\"dole\">")), "<p>.*?<a href=\"(.*?)\" .*?>(.*?)</a>")
                val result = ArrayList<Story>()

                for (row in parsed.items) {
                    result.add(Story(row[1].substring(row[1].indexOf(".")+1).trim(), row[0]))
                }

                return@map result
            }
    }
}
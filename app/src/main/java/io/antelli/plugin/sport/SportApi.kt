package io.antelli.plugin.sport

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.sport.entity.SportResult
import io.reactivex.Observable


class SportApi : BaseWebApi() {

    companion object {
        val MODE_ALL = 0
        val MODE_FOOTBALL = 1
        val MODE_HOCKEY = 2
    }

    fun getResults(mode : Int, team : String?): Observable<ArrayList<SportResult>> {
        when (mode){
            MODE_ALL -> return getResultsFrom("https://www.sport.cz", team)
            MODE_FOOTBALL -> return getResultsFrom("https://www.sport.cz/fotbal", team)
            MODE_HOCKEY -> return getResultsFrom("https://www.sport.cz/hokej", team)
        }
        return Observable.empty()
    }

    private fun getResultsFrom(link: String, team: String?): Observable<ArrayList<SportResult>> {

        return getHtmlFrom(link)
                .map { html ->
                    val result = ArrayList<SportResult>()
                    if (html != null) {
                        val tableIndex = html.indexOf("result-table result-mix")
                        val indexEndTable = html.indexOf("</table>", tableIndex)

                        var currentLeague: String? = null
                        var parsed = parse(html.substring(tableIndex, indexEndTable), "<tr(.*?)>(.*?)</tr>")
                        for (row in parsed.items) {
                            var tr = row[1]
                            if (row[0].contains("heading")) {
                                currentLeague = removeHtmlTags(row[1])
                            } else if(!row[0].contains("line")){
                                var parsedTr = parse(tr, "date\">(.*?)</td.*?nowrap\">(.*?)</td.*?nowrap\">(.*?)<.*?src=\"(.*?)\".*?src=\"(.*?)\".*?nowrap\">(.*?)<.*?\"score.*?>(.*?)</td")
                                var item = SportResult()
                                if (!parsedTr.isEmpty) {
                                    item.date = removeHtmlTags(parsedTr.first(0))
                                    item.event = parsedTr.first(1)
                                    item.homeTeam = parsedTr.first(2)
                                    item.homeTeamLogo = parsedTr.first(3).replace("/21/", "/75/")
                                    item.awayTeamLogo = parsedTr.first(4).replace("/21/", "/75/")
                                    item.awayTeam = parsedTr.first(5)
                                    item.score = removeHtmlTags(parsedTr.first(6))
                                    item.league = currentLeague

                                    if (team == null || ((item.homeTeam?.toLowerCase()!!.contains(team)) || item.awayTeam?.toLowerCase()!!.contains(team))){
                                        result.add(item)
                                    }

                                } else {
                                    var parsedUpcoming = parse(tr, "date\">(.*?)</td.*?online\">(.*?)</td.*?home\">(.*?)<.*?src=\"(.*?)\".*?src=\"(.*?)\".*?host\">(.*?)</td>")
                                    if (!parsedUpcoming.isEmpty) {
                                        item.date = removeHtmlTags(parsedUpcoming.first(0))
                                        item.event = parsedUpcoming.first(1)
                                        item.homeTeam = parsedUpcoming.first(2)
                                        item.homeTeamLogo = parsedUpcoming.first(3).replace("/21/", "/75/")
                                        item.awayTeamLogo = parsedUpcoming.first(4).replace("/21/", "/75/")
                                        item.awayTeam = parsedUpcoming.first(5)
                                        item.score = "- : -"
                                        item.league = currentLeague
                                        item.upcoming = true
                                    }

                                    if (team == null || ((item.homeTeam?.toLowerCase()!!.contains(team)) || item.awayTeam?.toLowerCase()!!.contains(team))){
                                        result.add(item)
                                    }
                                }
                            }

                        }
                    }
                    return@map result

                }
    }
}
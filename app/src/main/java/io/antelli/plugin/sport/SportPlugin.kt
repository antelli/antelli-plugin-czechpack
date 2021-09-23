package io.antelli.plugin.sport

import android.widget.ImageView
import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.sport.entity.SportResult
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.*

class SportPlugin : BaseWebPlugin<SportApi>() {
    override fun initApi(): SportApi {
        return SportApi()
    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("výsledky", "výsledek", "jak dopad", "jak hrál", "kdy se hraje", "kdy hraje"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {

        var team: String? = null
        if (question.containsOne("jak hrál", "kdy hraje")) {
            team = question.removeWords("jak", "hrála", "hrálo", "hráli", "hrát", "hraje", "kdy", "bude", "má")
        }
        var mode = SportApi.MODE_ALL

        if (question.contains("fotbal")) {
            mode = SportApi.MODE_FOOTBALL
        } else if (question.contains("hokej")) {
            mode = SportApi.MODE_HOCKEY
        }

        api.getResults(mode, team)
                .map { response -> convert(response) }
                .subscribe(
                        { answer -> callback.answer(answer) },
                        { e -> callback.answer(ErrorAnswer()) }
                )
    }

    private fun convert(items: ArrayList<SportResult>): Answer {
        val result = Answer()
        for (item in items) {
            val answerItem = AnswerItem().apply {
                type = AnswerItem.TYPE_CARD
                image = item.homeTeamLogo
                imageScaleType = ImageView.ScaleType.FIT_CENTER
                secondaryImage = item.awayTeamLogo
                title = item.homeTeam + " - " + item.awayTeam
                text = item.date
                largeText = item.score
                subtitle = item.league
                speech = if (!item.upcoming) {
                    item.homeTeam + " " + item.awayTeam + " " + item.score
                } else {
                    item.homeTeam + " " + item.awayTeam + " hraje " + item.date
                }
            }
            result.addItem(answerItem)

        }
        return result
    }

}

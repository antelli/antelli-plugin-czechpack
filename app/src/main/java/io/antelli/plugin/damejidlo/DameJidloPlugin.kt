package io.antelli.plugin.damejidlo

import android.content.Intent
import android.net.Uri
import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.damejidlo.entity.Restaurant
import io.antelli.plugin.damejidlo.entity.SearchResult
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.*
import javax.security.auth.callback.Callback

class DameJidloPlugin : BaseWebPlugin<DameJidloApi>() {

    companion object {
        const val STAE_IDLE = 0
        const val STAE_PICK_FOOD = 1
    }

    var state = STAE_IDLE

    override fun initApi(): DameJidloApi {
        return DameJidloApi()
    }

    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        if (state == STAE_IDLE) {
            callback.canAnswer(question.containsOne("dal bych si", "dala bych si", "jídlo", "mam hlad", "mám hlad", "mám chuť", "mam chuť", "bych si dal"))
        } else {
            callback.canAnswer(true)
        }
    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        if (question.containsOne("jídlo", "hlad", "něco")) {
            val result = Answer().apply {
                addItem(AnswerItem().apply {
                    text = "Co by sis dal?"
                    speech = "Co by sis dal?"
                    addHint(Hint("Pizza"))
                    addHint(Hint("Sushi"))
                    addHint(Hint("Burger"))
                    addHint(Hint("Kebab"))
                    addHint(Hint("Řízek"))
                    addHint(Hint("Těstoviny"))
                    addHint(Hint("Salát"))
                })
            }
            state = STAE_PICK_FOOD
            callback.answer(result)
        } else {
            val query = question.removeWords("dal bych si", "dala bych si", "třeba", "mám chuť na", "mam chuť na")
            /*api.search(query)
                    .map { convert(it) }
                    .subscribe({callback.answer(it)},{callback.answer(ErrorAnswer())})*/
            callback.answer(Answer().apply {
                autoRun = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.damejidlo.cz/search?q=$query"))
                addItem(AnswerItem().apply {
                    text = "Hledám $query přes Dáme Jídlo"
                    speech = "Hledám $query přes Dáme Jídlo"
                })
            })
            reset()
        }
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun reset() {
        state = STAE_IDLE
    }

    fun convert(searchResult: SearchResult): Answer {
        val result = Answer()
        val restaurantsItem = AnswerItem().apply {
            type = AnswerItem.TYPE_CAROUSEL_MEDIUM
        }

        for (r in searchResult.restaurants) {
            restaurantsItem.addItem(convert(r))
        }

        result.addItem(restaurantsItem)
        return result
    }

    fun convert(restaurant: Restaurant): AnswerItem {
        return AnswerItem().apply {
            title = restaurant.name
            image = "https://www.damejidlo.cz" + restaurant.image
            subtitle = restaurant.rating
            text = restaurant.deliveryPrice
        }
    }
}
package io.antelli.plugin.recepty

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException

import java.util.ArrayList

import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.recepty.entity.Recipe
import io.antelli.plugin.recepty.entity.RecipeDetail
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Hint
import io.antelli.sdk.model.Question
import io.reactivex.Observable
import kotlin.reflect.KClass

class ReceptyPlugin : BaseWebPlugin<ReceptyApi>() {

    override val settingsActivity = ReceptySettingsActivity::class
    internal var keywords = arrayOf("jak se vaří", "jak se peče", "jak uvařit", "jak se dělá", "jak upéct", "jak uvařim", "jak uvařím", "jak uvařit", "jak udělat", "recept na", "recept")
    internal var removeWords = arrayOf("najdi mi", "najdi", "hledej")

    override fun initApi(): ReceptyApi {
        return ReceptyApi()
    }

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne(*keywords))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {

        Observable.just(question)
                .flatMap { q -> api.getRecipes(q.removeWords(keywords, removeWords)) }
                .map { r -> convert(r) }
                .subscribe({ answer -> callback.answer(answer) },
                        { throwable -> callback.answer(ErrorAnswer()) })
    }

    override fun command(command: Command, callback: IAnswerCallback) {

        if (command.action == ACTION_DETAIL) {
            Observable.just(command)
                    .flatMap { c -> api.getDetail(command.getString(PARAM_LINK)) }
                    .map { r -> convertDetail(r) }
                    .subscribe({ answer -> callback.answer(answer) },
                            { throwable -> callback.answer(ErrorAnswer()) })
        }
    }

    private fun convertDetail(detail: RecipeDetail): Answer {
        val groceryProvider = Prefs.receptyGroceryService

        val result = Answer()
        result.addItem(AnswerItem().apply {
            title = detail.name
            subtitle = "Hodnocení: " + detail.rating
            text = "Doba přípravy: " + detail.time
            type = AnswerItem.TYPE_CARD
        })

        if (detail.ingredients != null) {
            for (ingredientGroup in detail.ingredients!!) {
                val groupItem = AnswerItem().apply {
                    type = AnswerItem.TYPE_CARD
                    title = if (ingredientGroup.name != null && !ingredientGroup.name!!.isEmpty()) ingredientGroup.name else null
                    subtitle = "Suroviny"
                }

                val hints = ArrayList<Hint>()
                if (ingredientGroup.ingredients != null) {
                    val sb = StringBuilder()
                    for (ingredient in ingredientGroup.ingredients!!) {
                        if (ingredient.name != null) {
                            sb.append((if (ingredient.quantity != null) ingredient.quantity + " " else "") + ingredient.name + "\n")
                            var link: String? = null
                            when (groceryProvider) {
                                Prefs.GROCERY_PROVIDER_TESCO -> link = "https://nakup.itesco.cz/groceries/cs-CZ/search?query=" + ingredient.name!!.replace(" ", "%20")
                                Prefs.GROCERY_PROVIDER_ROHLIK -> link = "https://www.rohlik.cz/hledat/" + ingredient.name!!.replace(" ", "%20")
                                Prefs.GROCERY_PROVIDER_KOSIK -> link = "https://www.kosik.cz/hledat?q=" + ingredient.name!!.replace(" ", "+")
                            }
                            hints.add(Hint(ingredient.name, Command(Intent(Intent.ACTION_VIEW, Uri.parse(link)))))
                        }
                    }
                    groupItem.text = sb.toString().trim { it <= ' ' }
                    groupItem.speech = (groupItem.title ?: "") + " " + groupItem.subtitle + ": " + groupItem.text?.replace(" ks", " kusů")
                }
                groupItem.hints = hints
                result.addItem(groupItem)
            }
            if (detail.steps != null) {
                for (cookingStep in detail.steps!!) {
                    result.addItem(AnswerItem().apply {
                        type = AnswerItem.TYPE_CARD
                        text = cookingStep.description
                        speech = cookingStep.description
                    })
                }
            }
        }

        if (detail.similarRecipes != null && detail.similarRecipes!!.size != 0) {
            val similarItems = AnswerItem().apply {
                type = AnswerItem.TYPE_CAROUSEL_MEDIUM
                title = "Podobné recepty"
            }
            val subItems = ArrayList<AnswerItem>()
            for (recipe in detail.similarRecipes!!) {
                subItems.add(AnswerItem().apply {
                    title = recipe.name
                    image = recipe.image
                    command = Command(ACTION_DETAIL).apply {
                        putString(PARAM_LINK, recipe.link)
                    }
                })
            }
            similarItems.items = subItems
            result.addItem(similarItems)
        }

        return result
    }

    private fun convert(recipes: List<Recipe>?): Answer {
        val result = Answer()
        val text = "Tady jsou recepty, které jsem našla"
        result.addItem(AnswerItem().apply {
            this.text = text
            speech = text
        })

        if (recipes != null) {
            for (recipe in recipes) {
                result.addItem(AnswerItem().apply {
                    title = recipe.name
                    subtitle = recipe.time
                    this.text = recipe.text
                    speech = recipe.name
                    image = recipe.image
                    type = AnswerItem.TYPE_CARD
                    command = Command(ACTION_DETAIL).apply {
                        putString(PARAM_LINK, recipe.link)
                    }
                })
            }
        }
        return result
    }

    companion object {
        var ACTION_DETAIL = "DETAIL"
        var PARAM_LINK = "LINK"
    }
}

package io.antelli.plugin.recepty

import android.text.Html

import java.util.ArrayList

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.recepty.entity.CookingStep
import io.antelli.plugin.recepty.entity.Ingredient
import io.antelli.plugin.recepty.entity.IngredientGroup
import io.antelli.plugin.recepty.entity.Recipe
import io.antelli.plugin.recepty.entity.RecipeDetail
import io.reactivex.Observable

class ReceptyApi : BaseWebApi() {


    fun getRecipes(query: String): Observable<List<Recipe>> {
        return Observable.just(query)
                .flatMap { q -> getHtmlFrom("https://www.recepty.cz/vyhledavani?text=" + query.replace(" ", "+")) }
                .map { this.parseSearchResults(it) }
    }

    private fun parseSearchResults(html: String): List<Recipe> {
        val result = ArrayList<Recipe>()
        val tableIndex = html.indexOf("<div class=\"search-results__wrapper\">")

        if (tableIndex != -1) {
            val table = html.substring(tableIndex, html.indexOf("search-menu__item tabs__tab", tableIndex))

            val parsed = parse(table, "<div class=\"recommended-recipes__item\">.*?href=\"(.*?)\".*?src=\"(.*?)\".*?title=\"(.*?)\".*?recommended-recipes__perex\">(.*?)<a")

            for (i in 0 until parsed.size()) {
                val row = parsed.getRow(i)
                val item = Recipe()
                item.link = row[0]
                item.image = row[1]
                item.name = Html.fromHtml(row[2]).toString()
                item.text = row[3].trim { it <= ' ' }
                result.add(item)

                if (i == 10) {
                    break
                }
            }
        }
        return result
    }

    fun getDetail(link: String?): Observable<RecipeDetail> {
        return Observable.just(link)
                .flatMap { url -> getHtmlFrom("https://www.recepty.cz$url") }
                .map{ this.parseRecipeDetail(it) }
    }

    private fun parseRecipeDetail(html: String): RecipeDetail {
        val result = RecipeDetail()
        val parsed = parse(html, "<h1 class=\"recipe-title__title\">(.*?)</h1>.*?rating--overall-row.*?<p>(.*?)</p>.*?recipe-header__time.*?<span>(.*?)</span>")

        if (!parsed.isEmpty) {
            result.name = parsed.get(0, 0)
            result.rating = parsed.get(0, 1)
            result.time = parsed.get(0, 2)
        }

        result.ingredients = parseIngredientGroups(html)
        result.steps = parseSteps(html)
        result.similarRecipes = parseSimilarRecipes(html)

        return result
    }

    private fun parseIngredientGroups(html: String): ArrayList<IngredientGroup> {
        val result = ArrayList<IngredientGroup>()
        var done = false
        var startIdx = 0
        while (!done) {
            startIdx = html.indexOf("ingredient-assignment__group", startIdx)
            var endIdx = html.indexOf("ingredient-assignment__group", startIdx + 1)

            if (endIdx == -1) {
                endIdx = html.indexOf("<script>", startIdx)
                done = true
            }

            val parsed = parse(html.substring(startIdx, endIdx), "__group\">(.*?)<ul>(.*?)</ul>")
            val group = IngredientGroup()

            if (!parsed.isEmpty) {
                group.name = parsed.first(0)
                group.ingredients = parseIngredients(parsed.first(1))
            }
            result.add(group)
            startIdx++
        }
        return result
    }

    private fun parseIngredients(html: String): ArrayList<Ingredient> {
        val result = ArrayList<Ingredient>()
        val parsedIngredients = parse(html, "<li.*?>(.*?)</li>")
        for (rowIngr in parsedIngredients.items) {
            result.add(parseIngredient(rowIngr[0]))
        }
        return result
    }

    private fun parseIngredient(html: String): Ingredient {
        val result = Ingredient()
        if (html.contains("__quantity")) {
            val parsed = parse(html, "_quantity\">(.*?)</div>(.*?)</div>")
            if (!parsed.isEmpty) {
                val splitted = parsed.first(1).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (splitted.size == 1) {
                    result.quantity = parsed.first(0)
                    result.name = parsed.first(1)
                } else if (splitted.size == 2) {
                    result.quantity = parsed.first(0) + " " + splitted[0]
                    result.name = splitted[1].trim { it <= ' ' }
                } else if (splitted.size == 3) {
                    result.quantity = parsed.first(0) + " " + splitted[0]
                    result.name = splitted[1].trim { it <= ' ' }
                    result.note = removeHtmlTags(splitted[2].trim { it <= ' ' }).trim { it <= ' ' }
                }
            }
        } else {
            val parsed = parse(html, "desc\">(.*?)</div>")
            if (!parsed.isEmpty) {
                if (parsed.first(0).contains("<em>")) {
                    val parsedEm = parse(parsed.first(0), "(.*?)<em>(.*?)</em>")
                    if (!parsedEm.isEmpty) {
                        result.name = parsedEm.first(0)
                        result.quantity = parsedEm.first(1)
                    }
                } else {
                    result.name = removeHtmlTags(parsed.first(0))
                }
            }
        }
        return result
    }

    private fun parseSteps(html: String): List<CookingStep> {
        val htmlPart = html.substring(html.indexOf("cooking-process__item-wrapper"), html.indexOf("cooking-process__footer"))
        val parsed = parse(htmlPart, "cooking-process__item.*?cooking-process__number\">(.*?)</div>.*?paragraph-.*?>(.*?)</div>")
        val result = ArrayList<CookingStep>()

        for (row in parsed.items) {
            result.add(CookingStep(Integer.valueOf(row[0]), row[1]))
        }
        return result
    }

    private fun parseSimilarRecipes(html: String): ArrayList<Recipe> {

        val parsed = parse(html, "<div class=\"similar-recipes-item\">.*?data-src=\"(.*?)\".*?title=\"(.*?)\".*?href=\"(.*?)\"")

        val result = ArrayList<Recipe>()

        for (row in parsed.items) {
            val recipe = Recipe()
            recipe.image = row[0]
            recipe.name = row[1]
            recipe.link = row[2]
            result.add(recipe)
        }
        return result
    }
}

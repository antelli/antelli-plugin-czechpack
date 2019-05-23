package io.antelli.plugin.recepty.entity

import java.util.ArrayList

class RecipeDetail : Recipe() {

    var rating: String? = null
    var ingredients: ArrayList<IngredientGroup>? = null
    var similarRecipes: ArrayList<Recipe>? = null
    var steps: List<CookingStep>? = null

    fun addIngredientsGroup(group: IngredientGroup) {
        if (ingredients == null) {
            ingredients = ArrayList()
        }
        ingredients!!.add(group)
    }
}

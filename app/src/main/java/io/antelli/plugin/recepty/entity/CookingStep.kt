package io.antelli.plugin.recepty.entity

class CookingStep(number: Int, description: String) {

    var number: Int = 0
    var description: String? = null

    init {
        this.number = number
        this.description = description
    }
}

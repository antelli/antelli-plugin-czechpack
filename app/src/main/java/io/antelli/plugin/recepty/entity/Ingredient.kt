package io.antelli.plugin.recepty.entity

class Ingredient {
    var name: String? = null
    var quantity: String? = null
    var note: String? = null

    constructor() {}

    constructor(name: String, quantity: String) {
        this.name = name
        this.quantity = quantity
    }
}

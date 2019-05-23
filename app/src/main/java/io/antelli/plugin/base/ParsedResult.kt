package io.antelli.plugin.base

class ParsedResult(val items: List<List<String>>) {

    val isEmpty: Boolean
        get() = items.isEmpty()

    operator fun get(row: Int, col: Int): String {
        return items[row][col]
    }

    fun first(col: Int): String {
        return items[0][col]
    }

    fun getRow(row: Int): List<String> {
        return items[row]
    }

    fun size(): Int {
        return items.size
    }
}

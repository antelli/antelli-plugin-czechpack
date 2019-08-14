package io.antelli.plugin.seznam.pocasi.entity

class WeatherDescription(val icon : Int?, val condition : String) {

    fun getIconLink() : String {
        return "https://www.windy.com/img/icons4/png_25@2x/$icon.png"
    }
}
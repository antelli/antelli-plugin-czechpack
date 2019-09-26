package io.antelli.plugin.seznam.pocasi.entity

class WeatherDescription(val icon : Int?, val condition : String) {

    companion object{
        fun from(iconId : Int) : WeatherDescription{
            return when (iconId){
                0 -> WeatherDescription(1, "jasno")
                1 -> WeatherDescription(3, "polojasno")
                2 -> WeatherDescription(2, "skoro jasno")
                3 -> WeatherDescription(6, "skoro jasno, déšť")
                4 -> WeatherDescription(23, "skoro jasno, místy bouřky")
                10 -> WeatherDescription(4, "zataženo")
                11 -> WeatherDescription(7, "zataženo, déšť")
                18 -> WeatherDescription(3, "polojasno")
                19 -> WeatherDescription(6, "polojasno, déšť")
                12 -> WeatherDescription(14, "zataženo, bouřky")
                26 -> WeatherDescription(4, "zataženo")
                27 -> WeatherDescription(7, "zataženo, déšť")
                36 -> WeatherDescription(21, "polojasno, bouřky")
                else -> WeatherDescription(null, "")
            }
        }
    }

    fun getIconLink() : String {
        return "https://www.windy.com/img/icons4/png_25@2x/$icon.png"
    }
}
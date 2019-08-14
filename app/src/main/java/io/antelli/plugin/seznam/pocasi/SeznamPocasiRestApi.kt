package io.antelli.plugin.seznam.pocasi

import io.antelli.plugin.BaseRestApi
import io.antelli.plugin.seznam.pocasi.entity.WeatherDescription
import io.antelli.plugin.seznam.pocasi.entity.WeatherDto
import io.reactivex.Observable

class SeznamPocasiRestApi : BaseRestApi<SeznamPocasiApiDef>() {
    override val baseUrl
        get() = "https://wapi.pocasi.seznam.cz/"
    override val apiDefClass
        get() = SeznamPocasiApiDef::class.java

    fun getCurrent(lat : Double?, lon : Double?): Observable<WeatherDto> {
        val latitude = lat ?: 50.09
        val longitude = lon ?: 14.42
        return api.getForecast(String.format("%.2f", latitude), String.format("%.2f", longitude), "place_name,current")
    }

    fun getForecast(lat : Double?, lon : Double?): Observable<WeatherDto> {
        val latitude = lat ?: 50.09
        val longitude = lon ?: 14.42
        return api.getForecast(String.format("%.2f", latitude), String.format("%.2f", longitude), "place_name,current,daily,entries")
    }

    fun getWeatherCondition(id: Int?): WeatherDescription {
        return  when (id){
            0 -> WeatherDescription(1, "jasno")
            1 -> WeatherDescription(3, "polojasno")
            2 -> WeatherDescription(2, "skoro jasno")
            3 -> WeatherDescription(6, "polojasno, déšť")
            4 -> WeatherDescription(23, "skoro jasno, bouřky")
            10 -> WeatherDescription(4, "zataženo")
            11 -> WeatherDescription(7, "zataženo, déšť")
            12 -> WeatherDescription(14, "zataženo, bouřky")
            36 -> WeatherDescription(21, "polojasno, bouřky")
            else -> WeatherDescription(null, "")
        }
    }
}
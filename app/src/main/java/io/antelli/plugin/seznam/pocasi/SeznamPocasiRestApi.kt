package io.antelli.plugin.seznam.pocasi

import io.antelli.plugin.BaseRestApi
import io.antelli.plugin.seznam.pocasi.entity.WeatherDescription
import io.antelli.plugin.seznam.pocasi.entity.Weather
import io.reactivex.Observable
import java.util.*
import kotlin.reflect.KClass

class SeznamPocasiRestApi : BaseRestApi<SeznamPocasiApiDef>() {
    override val baseUrl
        get() = "https://wapi.pocasi.seznam.cz/"
    override val apiDefClass : KClass<SeznamPocasiApiDef>
            get() = SeznamPocasiApiDef::class

    fun getCurrent(lat : Double?, lon : Double?): Observable<Weather> {
        val latitude = lat ?: 50.09
        val longitude = lon ?: 14.42
        return api.getForecast(String.format(Locale.US,"%.2f", latitude), String.format(Locale.US,"%.2f", longitude), "place_name,current")
    }

    fun getForecast(lat : Double?, lon : Double?): Observable<Weather> {
        val latitude = lat ?: 50.09
        val longitude = lon ?: 14.42
        return api.getForecast(String.format(Locale.US,"%.2f", latitude), String.format(Locale.US,"%.2f", longitude), "place_name,current,daily,entries")
    }
}
package io.antelli.plugin.seznam.pocasi

import io.antelli.plugin.seznam.pocasi.entity.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface SeznamPocasiApiDef {

    @GET("v2/forecast")
    fun getForecast(@Query("lat") lat: String, @Query("lon") lon: String, @Query("include") include: String): Observable<Weather>

}
package io.antelli.plugin.inpocasi

import io.antelli.plugin.inpocasi.dto.WeatherDto
import retrofit2.http.GET

interface InPocasiApiDef {

    @GET("xml.php?id=a11b86925a")
    suspend fun getWeather() : WeatherDto
}
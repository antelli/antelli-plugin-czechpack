package io.antelli.plugin.inpocasi

import io.antelli.plugin.BaseXmlApi
import io.antelli.plugin.inpocasi.entity.RegionDay
import io.antelli.plugin.inpocasi.entity.RegionDayWeather
import io.antelli.plugin.inpocasi.entity.WeatherData
import io.antelli.plugin.inpocasi.entity.WeatherDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

class InPocasiRepository : BaseXmlApi<InPocasiApiDef>() {

    override val baseUrl: String
        get() = "https://www.in-pocasi.cz/pocasi-na-web/"
    override val apiDefClass: KClass<InPocasiApiDef>
        get() = InPocasiApiDef::class

    suspend fun getWeather(): WeatherData {
        val response = api.getWeather()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return WeatherData(response.forecasts.map { WeatherDay(it.day, dateFormat.parse(it.date), it.status, it.temperatureDay, it.temperatureNight, it.text) }, response.regions.map { RegionDay(it.day, dateFormat.parse(it.date), it.region.map { RegionDayWeather(it.id, it.data.status, it.data.temperatureDay) }) })
    }

}
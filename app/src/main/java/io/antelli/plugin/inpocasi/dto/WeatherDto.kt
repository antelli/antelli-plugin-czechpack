package io.antelli.plugin.inpocasi.dto

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "pocasi", strict = false)
class WeatherDto(
        @field:ElementList(name = "predpoved", inline = true)
        @param:ElementList(name = "predpoved", inline = true)
        val forecasts: List<ForecastDayDto>,

        @field:ElementList(name = "kraje", inline = true)
        @param:ElementList(name = "kraje", inline = true)
        val regions: List<RegionsDto>,
)
package io.antelli.plugin.inpocasi.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "predpoved", strict = false)
class ForecastDayDto (
        @field:Attribute(name = "id")
        @param:Attribute(name = "id")
        val id: Int,

        @field:Attribute(name = "den")
        @param:Attribute(name = "den")
        val day: String,

        @field:Attribute(name = "datum")
        @param:Attribute(name = "datum")
        val date: String,

        @field:Element(name = "stav")
        @param:Element(name = "stav")
        val status: String,

        @field:Element(name = "teplotaden")
        @param:Element(name = "teplotaden")
        val temperatureDay: Int,

        @field:Element(name = "teplotanoc")
        @param:Element(name = "teplotanoc")
        val temperatureNight: Int,

        @field:Element(name = "text")
        @param:Element(name = "text")
        val text: String,


)
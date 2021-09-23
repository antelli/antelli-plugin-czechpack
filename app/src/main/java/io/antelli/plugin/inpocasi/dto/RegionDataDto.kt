package io.antelli.plugin.inpocasi.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "data", strict = false)
class RegionDataDto (
        @field:Attribute(name = "stav")
        @param:Attribute(name = "stav")
        val status: String,

        @field:Attribute(name = "teplotaDen")
        @param:Attribute(name = "teplotaDen")
        val temperatureDay: Int
)
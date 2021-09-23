package io.antelli.plugin.inpocasi.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "kraje", strict = false)
class RegionsDto (
        @field:Attribute(name = "id")
        @param:Attribute(name = "id")
        val id: Int,

        @field:Attribute(name = "den")
        @param:Attribute(name = "den")
        val day: String,

        @field:Attribute(name = "datum")
        @param:Attribute(name = "datum")
        val date: String,

        @field:ElementList(name = "kraj", inline = true)
        @param:ElementList(name = "kraj", inline = true)
        val region: List<RegionDto>
)
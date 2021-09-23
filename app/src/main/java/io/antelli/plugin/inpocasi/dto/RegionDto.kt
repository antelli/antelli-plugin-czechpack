package io.antelli.plugin.inpocasi.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "kraj", strict = false)
class RegionDto (
        @field:Attribute(name = "id")
        @param:Attribute(name = "id")
        val id: String,

        @field:Element(name = "data")
        @param:Element(name = "data")
        val data: RegionDataDto
)
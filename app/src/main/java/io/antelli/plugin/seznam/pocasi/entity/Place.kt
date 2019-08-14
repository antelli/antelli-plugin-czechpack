package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("isDay")
    val isDay: Boolean?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("localNow")
    val localNow: String?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("name")
    val name: Name?,
    @SerializedName("seo")
    val seo: Seo?,
    @SerializedName("TZoffset")
    val tZoffset: Int?
)
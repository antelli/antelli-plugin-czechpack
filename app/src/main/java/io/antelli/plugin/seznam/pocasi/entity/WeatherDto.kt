package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("current")
    val current: Current?,
    @SerializedName("current_aprox")
    val currentAprox: Current?,
    @SerializedName("daily")
    val daily: List<Daily>?,
    @SerializedName("entries")
    val entries: List<Entry>?,
    @SerializedName("place")
    val place: Place?
)
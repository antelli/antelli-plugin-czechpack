package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("bio")
    val bio: Int,
    @SerializedName("feelTemp")
    val feelTemp: Double,
    @SerializedName("feelTemp2")
    val feelTemp2: Double,
    @SerializedName("icon")
    val icon: Int,
    @SerializedName("isDay")
    val isDay: Boolean,
    @SerializedName("precip")
    val precip: Double,
    @SerializedName("pressure")
    val pressure: Double,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("wind")
    val wind: Double,
    @SerializedName("windDir")
    val windDir: Int,
    @SerializedName("windGust")
    val windGust: Double
)
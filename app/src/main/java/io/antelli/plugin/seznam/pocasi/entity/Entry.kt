package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class Entry(
    @SerializedName("dayId")
    val dayId: Int?,
    @SerializedName("icon")
    val icon: Int?,
    @SerializedName("isDay")
    val isDay: Boolean?,
    @SerializedName("localDate")
    val localDate: String?,
    @SerializedName("localTime")
    val localTime: String?,
    @SerializedName("precip")
    val precip: Double?,
    @SerializedName("snowPrecip")
    val snowPrecip: Double?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("wind")
    val wind: Double?,
    @SerializedName("windDir")
    val windDir: Int?
)
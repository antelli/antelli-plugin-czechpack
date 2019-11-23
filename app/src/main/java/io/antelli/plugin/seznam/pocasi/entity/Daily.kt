package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName
import java.util.*

data class Daily(
        @SerializedName("dataHours")
    val dataHours: Int,
        @SerializedName("icon")
    val icon: Int,
        @SerializedName("localDate")
    val localDate: Date,
        @SerializedName("snowPrecip")
    val snowPrecip: Double,
        @SerializedName("sunrise")
    val sunrise: String,
        @SerializedName("sunset")
    val sunset: String,
        @SerializedName("tempMax")
    val tempMax: Double,
        @SerializedName("tempMin")
    val tempMin: Double
)
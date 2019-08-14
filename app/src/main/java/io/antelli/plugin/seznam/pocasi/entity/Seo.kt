package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class Seo(
    @SerializedName("isCz")
    val isCz: Boolean?,
    @SerializedName("placeId")
    val placeId: String?,
    @SerializedName("placeName")
    val placeName: String?,
    @SerializedName("source")
    val source: String?
)
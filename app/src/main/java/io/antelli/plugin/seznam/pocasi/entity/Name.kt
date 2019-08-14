package io.antelli.plugin.seznam.pocasi.entity


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("area")
    val area: String?,
    @SerializedName("coun")
    val coun: String?,
    @SerializedName("dist")
    val dist: String?,
    @SerializedName("muni")
    val muni: String?,
    @SerializedName("regi")
    val regi: String?,
    @SerializedName("ward")
    val ward: String?
)
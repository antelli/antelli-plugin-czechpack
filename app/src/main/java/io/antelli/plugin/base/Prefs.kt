package io.antelli.plugin.base

import android.content.Context
import android.content.SharedPreferences
import io.antelli.plugin.App

import io.antelli.plugin.idnes.tv.IdnesTvChannel

/**
 * Created by stepan on 02.09.2017.
 */

object Prefs {

    private const val SEZNAM_POCASI_CITY = "SEZNAM_POCASI_CITY"
    private const val SEZNAM_POCASI_GPS = "SEZNAM_POCASI_GPS"
    private const val IDNES_TV_CHANNELS = "IDNES_TV_CHANNELS"
    private const val RECEPTY_GROCERY_SERVICE = "RECEPTY_GROCERY_SERVICE"


    val GROCERY_PROVIDER_TESCO = "Tesco"
    val GROCERY_PROVIDER_ROHLIK = "Rohlík"
    val GROCERY_PROVIDER_KOSIK = "Košík"

    private val prefs = App.get().getSharedPreferences("plugins-cs", Context.MODE_PRIVATE)

    var seznamPocasiCity: String
        get() = load(SEZNAM_POCASI_CITY, "praha")
        set(value) {
            save(SEZNAM_POCASI_CITY, value)
        }

    var seznamPocasiGps: Boolean
        get() = load(SEZNAM_POCASI_GPS, false)
        set(value) {
            save(SEZNAM_POCASI_GPS, value)
        }

    val idnesTvChannels: String
        get() = load(IDNES_TV_CHANNELS, "1,2,3,4,78,330,302,332,92,226,331,474,89,95,18,463,24,19")

    var receptyGroceryService: String
        get() = load(RECEPTY_GROCERY_SERVICE, GROCERY_PROVIDER_TESCO)
        set(service) {
            save(RECEPTY_GROCERY_SERVICE, service)
        }

    fun setIdnesTvChannels(channels: List<IdnesTvChannel>) {
        val sb = StringBuilder()
        for (i in channels.indices) {
            if (channels[i] != null) {
                sb.append(Integer.toString(channels[i].id))
                if (i < channels.size - 1) {
                    sb.append(",")
                }
            }
        }
        save(IDNES_TV_CHANNELS, sb.toString())
    }

    private fun save(name: String, value: Boolean) {
        edit().putBoolean(name, value).apply()
    }

    private fun save(name: String, value: Int) {
        edit().putInt(name, value).apply()
    }

    private fun save(name: String, value: Long) {
        edit().putLong(name, value).apply()
    }

    private fun save(name: String, value: String) {
        edit().putString(name, value).apply()
    }

    private fun load(name: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(name, defaultValue)
    }

    private fun load(name: String, defaultValue: Int): Int {
        return prefs.getInt(name, defaultValue)
    }

    private fun load(name: String, defaultValue: Long): Long {
        return prefs.getLong(name, defaultValue)
    }

    private fun load(name: String, defaultValue: String): String {
        return prefs.getString(name, defaultValue)
    }

    private fun edit(): SharedPreferences.Editor {
        return prefs.edit()
    }

}

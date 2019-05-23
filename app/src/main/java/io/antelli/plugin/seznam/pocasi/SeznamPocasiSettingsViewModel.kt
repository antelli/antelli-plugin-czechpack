package io.antelli.plugin.seznam.pocasi

import android.Manifest
import android.app.Application

import framework.viewmodel.BaseViewModel

import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.base.AntelliLocation

/**
 * Handcrafted by Štěpán Šonský on 12.11.2017.
 */

class SeznamPocasiSettingsViewModel(application: Application) : BaseViewModel(application) {

    var isGps: ObservableField<Boolean> = ObservableField(false)
    var locations = ObservableArrayList<AntelliLocation>()
    var selectedIndex = ObservableInt()
    private val dataManager: SeznamPocasiApi

    init {
        dataManager = SeznamPocasiApi(getApplication())
        locations.addAll(dataManager.locations)

        val city = Prefs.seznamPocasiCity
        for (i in locations.indices) {
            if (locations[i].value == city) {
                selectedIndex.set(i)
                break
            }
        }

        val gpsSettings = Prefs.seznamPocasiGps
        if (gpsSettings && ContextCompat.checkSelfPermission(this.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            isGps.set(true)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Prefs.seznamPocasiGps = isGps.get()!!
        Prefs.seznamPocasiCity = locations[selectedIndex.get()].value
    }

}

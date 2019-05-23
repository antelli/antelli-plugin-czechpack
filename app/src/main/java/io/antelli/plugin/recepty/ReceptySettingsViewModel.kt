package io.antelli.plugin.recepty

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import framework.viewmodel.BaseViewModel
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.base.Prefs.GROCERY_PROVIDER_KOSIK
import io.antelli.plugin.base.Prefs.GROCERY_PROVIDER_ROHLIK
import io.antelli.plugin.base.Prefs.GROCERY_PROVIDER_TESCO

/**
 * Handcrafted by Štěpán Šonský on 23.7.2018.
 */

class ReceptySettingsViewModel(application: Application) : BaseViewModel(application) {

    var groceryServices = ObservableArrayList<String>()
    var selectedIndex = ObservableInt()

    init {
        groceryServices.add(GROCERY_PROVIDER_TESCO)
        groceryServices.add(GROCERY_PROVIDER_ROHLIK)
        groceryServices.add(GROCERY_PROVIDER_KOSIK)
        selectedIndex.set(groceryServices.indexOf(Prefs.receptyGroceryService))

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Prefs.receptyGroceryService = groceryServices[selectedIndex.get()]
    }

}

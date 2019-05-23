package io.antelli.plugin.idnes.tv

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import framework.viewmodel.BaseViewModel

import java.text.Normalizer
import java.util.ArrayList
import java.util.Collections

import io.antelli.plugin.base.Prefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Handcrafted by Štěpán Šonský on 07.06.2018.
 */
class IdnesTvSettingsViewModel(application: Application) : BaseViewModel(application) {

    var allItems = ObservableArrayList<IdnesTvChannel>()
    var items = ObservableArrayList<IdnesTvChannel>()
    var loading = ObservableBoolean()
    internal var dataManager = IdnesTvApi()
    internal var favouriteChannels: MutableList<IdnesTvChannel> = ArrayList()
    var search = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        search.observeForever {
            filter()
        }
    }

    fun filter(){
        items.clear()
        if (search.value == null || search.value!!.isEmpty()){
            items.addAll(allItems)
        } else{
            for (item in allItems) {
                if (item.name.toLowerCase().startsWith(search.value!!.toLowerCase())) {
                    items.add(item)
                }
            }
        }
    }

    fun getChannels() {
        loading.set(true)
        dataManager.channelsForSettings.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { idnesTvChannels ->
                    val favIds = Prefs.idnesTvChannels.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (favId in favIds) {
                        if (favId != "") {
                            val ch = idnesTvChannels.getChannel(Integer.parseInt(favId))
                            if (ch != null) {
                                favouriteChannels.add(ch)
                            }
                        }
                    }
                    idnesTvChannels
                }.subscribe(
                        { idnesTvChannels ->
                            loading.set(false)
                            allItems.clear()
                            Collections.sort(idnesTvChannels.getChannelsList()) { o1, o2 -> Normalizer.normalize(o1.name, Normalizer.Form.NFD).compareTo(Normalizer.normalize(o2.name, Normalizer.Form.NFD)) }
                            allItems.addAll(idnesTvChannels.getChannelsList())
                            filter()
                        },
                        { throwable ->
                            loading.set(false)
                            throwable.printStackTrace()
                        }
                )
    }

    fun isFavourite(channel: IdnesTvChannel): Boolean {
        return favouriteChannels.contains(channel)
    }

    fun toggleFavourite(channel: IdnesTvChannel) {
        if (favouriteChannels.contains(channel)) {
            favouriteChannels.remove(channel)
        } else {
            favouriteChannels.add(channel)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        if (favouriteChannels.size != 0) {
            Prefs.setIdnesTvChannels(favouriteChannels)
        }
    }
}

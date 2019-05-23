package framework.viewmodel

import android.app.Application
import androidx.lifecycle.*
import framework.event.LiveEventMap
import framework.event.LiveEvent
import kotlin.reflect.KClass

/**
 * Created by Stepan on 11.10.2016.
 */

abstract class BaseViewModel(application : Application) : AndroidViewModel(application), LifecycleObserver {
    private val liveEventMap = LiveEventMap()

    fun <T : LiveEvent> subscribe(lifecycleOwner: LifecycleOwner, eventClass: KClass<T>, eventObserver: Observer<T>) {
        liveEventMap.subscribe(lifecycleOwner, eventClass, eventObserver)
    }

    protected fun <T : LiveEvent> publish(event: T) {
        liveEventMap.publish(event)
    }
}

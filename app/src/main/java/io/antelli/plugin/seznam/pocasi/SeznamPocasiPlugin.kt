package io.antelli.plugin.seznam.pocasi

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.RemoteException
import android.widget.ImageView
import androidx.core.content.ContextCompat
import io.antelli.plugin.BaseRestPlugin
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.seznam.pocasi.entity.Daily
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.reactivex.functions.Consumer
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

class SeznamPocasiPlugin : BaseRestPlugin<SeznamPocasiRestApi>() {


    override fun initApi(): SeznamPocasiRestApi {
        return SeznamPocasiRestApi()
    }

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("počasí", "předpověď", "jak bude", "jak má být", "jak má bejt"))
    }

    @Throws(RemoteException::class)
    override fun answer(question: Question, callback: IAnswerCallback) {
        val loc = getLocation()

        api.getForecast(loc?.latitude, loc?.longitude)
                .map {
                    Answer().apply {
                        addItem(AnswerItem().apply {
                            val condition = api.getWeatherCondition(it.current?.icon)
                            type = AnswerItem.TYPE_CARD
                            title = it.place?.name?.area
                            subtitle = "Aktuálně"
                            imageScaleType = ImageView.ScaleType.FIT_START
                            image = condition.getIconLink()
                            largeText = String.format("%.1f", it.current?.temp) + "°C"
                            text = "BIO: " + it.current?.bio + "\n" +
                                    "Srážky: " + String.format("%.1f", it.current?.precip) + " mm\n" +
                                    "Vítr: " + String.format("%.1f", it.current?.wind) + " m/s\n" +
                                    "Tlak: " + String.format("%.1f", it.current?.pressure) + " hPa"
                            speech = getTextForecast(it.daily!![0])
                        })
                        addItem(AnswerItem().apply {
                            type = AnswerItem.TYPE_CAROUSEL_SMALL
                            if (it.daily != null) {
                                for (day in it.daily) {
                                    addItem(AnswerItem().apply {
                                        imageScaleType = ImageView.ScaleType.FIT_CENTER
                                        image = api.getWeatherCondition(day.icon).getIconLink()
                                        title = SimpleDateFormat("EEEE d.M.").format(day.localDate)
                                        subtitle = day.tempMax!!.roundToInt().toString() + "°C / " + day.tempMin!!.roundToInt().toString() + "°C"
                                    })
                                }
                            }
                        })
                    }
                }.subscribe(Consumer { callback.answer(it) }, Consumer { it.printStackTrace() })
    }

    fun getLocation(): Location? {
        if (!Prefs.seznamPocasiGps || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null
        }
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    fun getTextForecast(daily : Daily): String {
        return api.getWeatherCondition(daily.icon).condition +
                ", nejvyšší denní teploty " + (daily.tempMax!!).roundToInt() + "°C"
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return SeznamPocasiSettingsActivity::class.java
    }

    override fun reset() {

    }
}

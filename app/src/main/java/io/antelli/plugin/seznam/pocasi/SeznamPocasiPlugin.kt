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
import io.antelli.plugin.base.util.DateTimeCortex
import io.antelli.plugin.seznam.pocasi.entity.Weather
import io.antelli.plugin.seznam.pocasi.entity.WeatherDescription
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.reactivex.functions.Consumer
import java.text.SimpleDateFormat
import java.util.*
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

        val day = getDayDelta(question)

        val disposable = api.getForecast(loc?.latitude, loc?.longitude)
                .map {
                    if (day == 0) {
                        Answer().apply {
                            addItem(getCurrentCondition(it))
                            addItem(getForecastItem(it))
                        }
                    } else {
                        Answer().apply {
                            addItem(getDayCondition(it, day))
                            addItem(getHourly(it, day))
                        }
                    }

                }.subscribe { callback.answer(it) }
    }

    fun getLocation(): Location? {
        if (!Prefs.seznamPocasiGps || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    fun getCurrentCondition(data: Weather): AnswerItem {
        return AnswerItem().apply {
            val condition = WeatherDescription.from(data.current.icon)
            type = AnswerItem.TYPE_CARD
            title = data.place.name?.area
            subtitle = "Aktuálně"
            imageScaleType = ImageView.ScaleType.FIT_START
            image = condition.getIconLink()
            largeText = data.current.temp.roundToInt().toString() + "°C"
            text = "BIO: " + data.current.bio + "\n" +
                    "Srážky: " + String.format("%.1f", data.current.precip) + " mm\n" +
                    "Vítr: " + String.format("%.1f", data.current.wind) + " m/s\n" +
                    "Tlak: " + String.format("%.1f", data.current.pressure) + " hPa"

            speech = getTextForecast(data, 0)
        }
    }

    fun getDayCondition(data: Weather, day: Int): AnswerItem {
        return AnswerItem().apply {
            val daily = data.daily.get(day)
            val condition = WeatherDescription.from(daily.icon)

            type = AnswerItem.TYPE_CARD
            title = data.place?.name?.area
            subtitle = SimpleDateFormat("EEEE d.M.yyyy", Locale.getDefault()).format(daily.localDate)
            imageScaleType = ImageView.ScaleType.FIT_START
            image = condition.getIconLink()
            largeText = daily.tempMax.roundToInt().toString() + "°C / " + daily.tempMin.roundToInt().toString() + "°C"

            speech = getTextForecast(data, day)
        }
    }

    fun getDayDelta(question: Question): Int {
        val calendar = DateTimeCortex.getDateTimeFromString(question)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.SECOND, 0)
        val today = Calendar.getInstance()
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.HOUR_OF_DAY, 0)

        var daysDelta = DateTimeCortex.daysBetween(today, calendar)
        if (daysDelta > 7) {
            daysDelta = 0
        }
        return daysDelta
    }

    fun getHourly(data: Weather, day: Int): AnswerItem {
        return AnswerItem().apply {
            type = AnswerItem.TYPE_CAROUSEL_SMALL
            for (hour in data.entries.filter { it.dayId == day }) {
                addItem(AnswerItem().apply {
                    //imageScaleType = ImageView.ScaleType.FIT_CENTER
                    //image = api.getWeatherCondition(hour.icon).getIconLink()
                    title = hour.localTime
                    subtitle = hour.temp!!.roundToInt().toString() + "°C"
                    text = hour.precip!!.roundToInt().toString() + " mm"
                })
            }
        }
    }

    fun getForecastItem(data: Weather): AnswerItem {
        return AnswerItem().apply {
            type = AnswerItem.TYPE_CAROUSEL_SMALL
            if (data.daily != null) {
                for (day in data.daily) {
                    addItem(AnswerItem().apply {
                        imageScaleType = ImageView.ScaleType.FIT_CENTER
                        image = WeatherDescription.from(day.icon).getIconLink()
                        title = SimpleDateFormat("EEEE d.M.").format(day.localDate)
                        subtitle = day.tempMax!!.roundToInt().toString() + "°C / " + day.tempMin!!.roundToInt().toString() + "°C"
                    })
                }
            }
        }
    }

    fun getTextForecast(data: Weather, day: Int): String {
        val daily = data.daily[day]

        return WeatherDescription.from(daily.icon).condition +
                ", nejvyšší denní teploty " + (daily.tempMax).roundToInt() + "°C"
    }

    override fun command(command: Command, callback: IAnswerCallback) {

    }

    override fun getSettingsActivity(): Class<out Activity>? {
        return SeznamPocasiSettingsActivity::class.java
    }

    override fun reset() {

    }
}

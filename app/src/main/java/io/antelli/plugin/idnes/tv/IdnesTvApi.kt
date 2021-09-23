package io.antelli.plugin.idnes.tv

import android.content.Intent
import android.net.Uri
import android.widget.ImageView

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.base.util.DateTimeCortex
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.reactivex.Observable

/**
 * Handcrafted by Štěpán Šonský on 21.11.2017.
 */

class IdnesTvApi : BaseWebApi() {
    internal var dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    internal var timeFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    internal var favorites: List<String>? = null
    internal var channels: IdnesTvChannels? = null

    internal var mobileVersionSet: Boolean = false
    internal var date: Date? = null

    val channelsForSettings: Observable<IdnesTvChannels>
        get() = Observable.just(mobileVersionSet)
                .flatMap<Boolean> { aBoolean ->
                    if (aBoolean) {
                        return@flatMap Observable.just(mobileVersionSet)
                    } else {
                        return@flatMap Observable.just(mobileVersionSet)
                                .flatMap {getHtmlFrom("$URL_BASE/?setver=touch")}
                                .map { true }
                                .doOnNext { aBoolean1 -> mobileVersionSet = true }
                    }
                }.flatMap { resp -> getChannels() }

    fun answer(question: Question): Observable<Answer> {
        date = null
        if (question.contains(" bud")) {
            date = DateTimeCortex.getDateTimeFromString(question).time
        }

        val finalDate = date
        return Observable.just(mobileVersionSet)
                .flatMap{
                    if (it) {
                        return@flatMap Observable.just(mobileVersionSet)
                    } else {
                        return@flatMap Observable.just(mobileVersionSet).flatMap{getHtmlFrom("$URL_BASE/?setver=touch")
                                .map { s -> true }
                                .doOnNext { mobileVersionSet = true }}
                    }
                }
                .flatMap { getChannels() }
                .flatMap { setChannelsCookie() }
                .flatMap { getProgramSource(finalDate) }
                .map { source -> processResponse(source) }
    }

    private fun setChannelsCookie(): Observable<String> {
        return getHtmlFrom("https://tvprogram.idnes.cz/_cookie.aspx?channels=" + Prefs.idnesTvChannels)
    }

    private fun getProgramSource(date: Date?): Observable<String> {
        return getHtmlFrom(URL_BASE + "/?setver=touch" + if (date != null) "&date=" + dateFormat.format(date) + "&time=" + timeFormat.format(date) else "")
    }

    private fun processResponse(source: String): Answer {

        val result = Answer()
        val m = Pattern.compile("class=\"art-tv\".*?href=\"(.*?)\".*?image:url\\('(.*?)'\\).*?\"data\">(.*?)<ul>(.*?)</ul>", Pattern.DOTALL).matcher(source)
        var matcherUpcoming: Matcher
        var matcherCurrent: Matcher

        var item: AnswerItem
        while (m.find()) {
            item = AnswerItem().apply {
                type = AnswerItem.TYPE_CARD
                imageScaleType = ImageView.ScaleType.FIT_CENTER
            }

            matcherCurrent = Pattern.compile("<h3>(.*?)</h3>.*?width:(.*?)%", Pattern.DOTALL).matcher(m.group(3))
            var hasCurrent = false

            val link = removeHtmlTags(m.group(1))
            val chId = Integer.parseInt(link.substring(link.indexOf("channel=") + 8, link.indexOf("&")))
            val ch = channels!!.getChannel(chId)
            item.image = ch?.logo

            if (matcherCurrent.find()) {
                hasCurrent = true
                val name = removeHtmlTags(matcherCurrent.group(1))
                item.title = ch?.name
                item.subtitle = name
                item.speech = name + " na " + ch?.name
                /*  if (!matcherCurrent.group(2).equals("NaN")) {
                    item.setCurrentPercent(Integer.parseInt(matcherCurrent.group(2)));
                } else {
                    item.setCurrentPercent(100);
                }*/
            }

            matcherUpcoming = Pattern.compile("<li>.*?class=\"b\">(.*?)</span>(.*?)</li>", Pattern.DOTALL).matcher(m.group(4))

            val sb = StringBuilder()
            var i = 0
            while (matcherUpcoming.find()) {
                sb.append(matcherUpcoming.group(1).trim { it <= ' ' } + " " + removeHtmlTags(matcherUpcoming.group(2).trim { it <= ' ' }) + "\n")
                if (i == 0 && !hasCurrent) {
                    item.title = channels?.getChannel(chId)?.name
                    item.speech = removeHtmlTags(matcherUpcoming.group(2).trim { it <= ' ' } + " na " + channels?.getChannel(chId)?.name)
                }
                i++
            }
            item.text = sb.toString().trim { it <= ' ' }

            item.command = Command(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            result.addItem(item)
        }
        return result
    }

    private fun getChannels(): Observable<IdnesTvChannels> {

        return if (channels != null) {
            Observable.just(channels!!)
        } else {
            getHtmlFrom("$URL_BASE/?add=channels")
                    .map { source ->
                        val m = Pattern.compile("<label class=\"entry\">.*?image:url\\('(.*?)'\\).*?<h3>(.*?)</h3>.*?id=\"(.*?)\".*?</label>", Pattern.DOTALL).matcher(source)

                        val result = IdnesTvChannels()
                        var ch: IdnesTvChannel
                        while (m.find()) {
                            ch = IdnesTvChannel()
                            ch.logo = m.group(1)
                            ch.name = m.group(2)
                            ch.id = Integer.parseInt(m.group(3))
                            result.addChannel(ch)
                        }
                        result
                    }.doOnNext { result -> channels = result }
        }
    }

    companion object {

        private val URL_BASE = "https://tvprogram.idnes.cz"
    }
}


package io.antelli.plugin.seznam.pocasi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.widget.ImageView

import java.util.ArrayList
import java.util.Calendar
import java.util.regex.Pattern

import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import io.antelli.plugin.base.AntelliLocation
import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.base.Prefs
import io.antelli.plugin.base.util.DateTimeCortex
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.reactivex.Observable

/**
 * Handcrafted by Štěpán Šonský on 11.11.2017.
 */

class SeznamPocasiApi(private val context: Context) : BaseWebApi() {
    lateinit var locations: ArrayList<AntelliLocation>

    private val location: String
        get() {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            @SuppressLint("MissingPermission")
            val nearestLocation = getNearestLocation(lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER), locations)

            return if (nearestLocation != null) {
                nearestLocation.name
            } else {
                "praha"
            }
        }

    init {
        initLocations()
    }

    fun answer(question: Question): Observable<Answer> {

        val link: String
        val queryLocation = getLocationFromQuery(question.query)
        link = if (queryLocation != null) {
            "https://pocasi.seznam.cz/$queryLocation"
        } else {
            if (Prefs.seznamPocasiGps && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
                "https://pocasi.seznam.cz/$location"
            } else {
                "https://pocasi.seznam.cz/" + Prefs.seznamPocasiCity
            }
        }

        return getHtmlFrom(link).map { source -> processResponse(question, source) }
    }

    fun command(command: Command): Observable<Answer> {
        return Observable.just(command)
                .map { cmd ->
                    val answer = Answer()

                    answer.addItem(getMainItem(cmd.getString(PARAM_SOURCE), cmd.getInt(PARAM_DELTA) ?: 0))
                    answer
                }
    }

    private fun processResponse(question: Question, source: String): Answer {

        val answer = Answer()
        val calendar = DateTimeCortex.getDateTimeFromString(question)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.SECOND, 0)
        val today = Calendar.getInstance()
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.HOUR_OF_DAY, 0)

        var daysDelta = daysBetween(today, calendar)
        if (daysDelta > 7) {
            daysDelta = 0
        }

        answer.addItem(getMainItem(source, daysDelta))
        if (daysDelta == 0) {
            answer.addItems(getForecast(source))
        }

        return answer
    }

    private fun getMainItem(source: String?, daysDelta: Int): AnswerItem {
        val item = AnswerItem()

        var m = Pattern.compile("<h1 id=\"title\".*?>(.*?)</h1>", Pattern.DOTALL).matcher(source)
        if (m.find()) {
            item.title = removeHtmlTags(m.group(1))
        }

        if (daysDelta == 0) {
            m = Pattern.compile("<div class=\"wrapper\">.*?<span class=\"dayName\">(.*?)</span>.*?<span class=\"date\">(.*?)</span>.*?<div class=\"info\">.*?<p>(.*?)</p>.*?<span class=\"value\">(.*?)</span>.*?class=\"weather weather(.*?) .*?\"", Pattern.DOTALL).matcher(source)
            while (m.find()) {
                item.subtitle = m.group(1) + " " + m.group(2)
                item.largeText = m.group(4) + "°C"
                item.text = removeHtmlTags(m.group(3))
                item.speech = item.text
                item.type = AnswerItem.TYPE_CARD

                item.image = getImageLink(m.group(5))
                item.imageScaleType = ImageView.ScaleType.FIT_CENTER
                return item
            }
        } else {
            m = Pattern.compile("<div class=\"wrapper\">.*?<span class=\"dayName\">(.*?)</span>.*?<span class=\"date\">(.*?)</span>.*?<span class=\"value\">(.*?)</span>.*?<span class=\"value\">(.*?)</span>.*?<div class=\"info\">.*?<p>(.*?)</p>.*?class=\"weather weather(.*?)\"", Pattern.DOTALL).matcher(source?.substring(source.indexOf("predpoved-zitra")))
            var i = 1
            while (m.find()) {

                if (i == daysDelta) {
                    item.subtitle = m.group(1) + " " + m.group(2)
                    item.largeText = m.group(3) + "°C / " + m.group(4) + "°C"
                    item.text = removeHtmlTags(m.group(5))
                    item.speech = item.text
                    item.type = AnswerItem.TYPE_CARD
                    item.image = getImageLink(m.group(6))
                    item.imageScaleType = ImageView.ScaleType.FIT_CENTER
                    return item
                }
                i++
            }
        }
        return item
    }

    private fun getForecast(source: String): List<AnswerItem> {
        val result = ArrayList<AnswerItem>()
        val m = Pattern.compile("<div class=\"wrapper\">.*?<span class=\"dayName\">(.*?)</span>.*?<span class=\"date\">(.*?)</span>.*?<span class=\"value\">(.*?)</span>.*?<span class=\"value\">(.*?)</span>.*?<div class=\"info\">.*?<p>(.*?)</p>.*?class=\"weather weather(.*?)\"", Pattern.DOTALL).matcher(source.substring(source.indexOf("predpoved-zitra")))

        val item = AnswerItem().apply {
            type = AnswerItem.TYPE_CAROUSEL_SMALL
        }
        //var galleryItem: AnswerItem
        val gallery = ArrayList<AnswerItem>()
        var i = 1
        while (m.find()) {
            gallery.add(AnswerItem().apply {
                title = m.group(1) + " " + m.group(2)
                subtitle = m.group(3) + "°C / " + m.group(4) + "°C"
                image = getImageLink(m.group(6))
                imageScaleType = ImageView.ScaleType.FIT_CENTER
                command = Command(ACTION_FORECAST).apply {
                    putInt(PARAM_DELTA, i)
                    putString(PARAM_SOURCE, source)
                }
            })
            i++
        }
        item.items = gallery
        result.add(item)

        return result
    }

    private fun getImageLink(linkIn: String): String {
        var imageLink = "https://pocasi.seznam.cz/img/icons/"
        if (linkIn.contains("night")) {
            imageLink = imageLink + "night/" + linkIn.replace("night", "").trim { it <= ' ' } + ".png"
        } else {
            imageLink = imageLink + "day/" + linkIn.trim { it <= ' ' } + ".png"
        }

        return imageLink
    }

    private fun getLocationFromQuery(query: String): String? {
        return if (query.contains("benešov"))
            "benesov"
        else if (query.contains("beroun"))
            "beroun"
        else if (query.contains("blansk"))
            "blansko"
        else if (query.contains(" brn"))
            "brno"
        else if (query.contains("bruntál"))
            "bruntal"
        else if (query.contains("břeclav"))
            "breclav"
        else if (query.contains("česk") && query.contains(" líp"))
            "ceska-lipa"
        else if (query.contains("česk") && query.contains(" třebov"))
            "ceska-trebova"
        else if (query.contains("budějovic"))
            "ceske-budejovice"
        else if (query.contains("krumlov"))
            "cesky-krumlov"
        else if (query.contains("děčín"))
            "decin"
        else if (query.contains("domažlic"))
            "domazlice"
        else if (query.contains("frýd") && query.contains(" míst"))
            "frydek-mistek"
        else if (query.contains(" havlíčk") && query.contains("brod"))
            "havlickuv-brod"
        else if (query.contains("hodonín"))
            "hodonin"
        else if (query.contains("hradec") || query.contains("hradci"))
            "hradec-kralove"
        else if (query.contains("cheb"))
            "cheb"
        else if (query.contains("chomutov"))
            "chomutov"
        else if (query.contains("chrudim"))
            "chrudim"
        else if (query.contains("jablon"))
            "jablonec"
        else if (query.contains("jeseník"))
            "jesenik"
        else if (query.contains("jičín") && !query.contains(" nov"))
            "jicin"
        else if (query.contains("jihlav"))
            "jihlava"
        else if (query.contains("jindřich") || query.contains(" hrad"))
            "jindrichuv-hradec"
        else if (query.contains("karlov") || query.contains("vary") || query.contains("varech"))
            "karlovy-vary"
        else if (query.contains("karvin"))
            "karvina"
        else if (query.contains("kladn"))
            "kladno"
        else if (query.contains("klatov"))
            "klatovy"
        else if (query.contains("kolín"))
            "kolin"
        else if (query.contains("koměříž"))
            "kromeriz"
        else if (query.contains("kutn") && query.contains(" hor"))
            "kutna-hora"
        else if (query.contains("liber"))
            "liberec"
        else if (query.contains("litoměřic"))
            "litomerice"
        else if (query.contains("loun"))
            "louny"
        else if (query.contains("mělní"))
            "melnik"
        else if (query.contains("boleslav"))
            "mlada-boleslav"
        else if (query.contains("most"))
            "most"
        else if (query.contains("náchod"))
            "nachod"
        else if (query.contains("jičín") && query.contains("nov"))
            "novy-jicin"
        else if (query.contains("nymbur"))
            "nymburk"
        else if (query.contains("olomouc"))
            "olomouc"
        else if (query.contains("opav"))
            "opava"
        else if (query.contains("ostrav"))
            "ostrava"
        else if (query.contains("pardubic"))
            "pardubice"
        else if (query.contains("pelhřimov"))
            "pelhrimov"
        else if (query.contains("písek") || query.contains("písku"))
            "pisek"
        else if (query.contains("plz"))
            "plzen"
        else if (query.contains("praha") || query.contains("praze"))
            "praha"
        else if (query.contains("prachatic"))
            "prachatice"
        else if (query.contains("prostějov"))
            "prostejov"
        else if (query.contains("přerov"))
            "prerov"
        else if (query.contains("příbram"))
            "pribram"
        else if (query.contains("rakovník"))
            "rakovnik"
        else if (query.contains("rokycan"))
            "rokycany"
        else if (query.contains("rychnov"))
            "rychnov-nad-kneznou"
        else if (query.contains("semil"))
            "semily"
        else if (query.contains("sokolov"))
            "sokolov"
        else if (query.contains("sušic"))
            "susice"
        else if (query.contains("svitav"))
            "svitavy"
        else if (query.contains("šumper"))
            "sumperk"
        else if (query.contains("tábo"))
            "tabor"
        else if (query.contains("tachov"))
            "tachov"
        else if (query.contains("teplic"))
            "teplice"
        else if (query.contains("trutnov"))
            "trutnov"
        else if (query.contains("třebíč"))
            "trebic"
        else if (query.contains("hradišt"))
            "uherske-hradiste"
        else if (query.contains("ústí") && query.contains("lab"))
            "usti-nad-labem"
        else if (query.contains("ústí") && query.contains("orlic"))
            "usti-nad-orlici"
        else if (query.contains("vsetín"))
            "vsetin"
        else if (query.contains("vyškov"))
            "vyskov"
        else if (query.contains("zlín"))
            "zlin"
        else if (query.contains("znojm"))
            "znojmo"
        else if (query.contains("žďár"))
            "zdar-nad-sazavou"
        else
            null
    }

    fun initLocations() {
        locations = ArrayList()
        locations.add(AntelliLocation("Benešov", "benesov", 49.784035, 14.687492))
        locations.add(AntelliLocation("Beroun", "beroun", 49.968889, 14.086136))
        locations.add(AntelliLocation("Blansko", "blansko", 49.366278, 16.648353))
        locations.add(AntelliLocation("Brno", "brno", 49.198307, 16.603668))
        locations.add(AntelliLocation("Bruntál", "bruntal", 49.989642, 17.464775))
        locations.add(AntelliLocation("Břeclav", "breclav", 48.754378, 16.880783))
        locations.add(AntelliLocation("Česká Lípa", "ceska-lipa", 50.680797, 14.537551))
        locations.add(AntelliLocation("Česká Třebová", "ceska-trebova", 49.903701, 16.445793))
        locations.add(AntelliLocation("České Budějovice", "ceske-budejovice", 48.978414, 14.481933))
        locations.add(AntelliLocation("Český Krumlov", "cesky-krumlov", 48.812742, 14.317047))
        locations.add(AntelliLocation("Děčín", "decin", 50.776419, 14.216888))
        locations.add(AntelliLocation("Domažlice", "domazlice", 49.441343, 12.932914))
        locations.add(AntelliLocation("Frýdek-Místek", "frydek-mistek", 49.685845, 18.371780))
        locations.add(AntelliLocation("Havlíčkův Brod", "havlickuv-brod", 49.605593, 15.580963))
        locations.add(AntelliLocation("Hodonín", "hodonin", 48.854776, 17.127975))
        locations.add(AntelliLocation("Hradec Králové", "hradec-kralove", 50.214261, 15.830505))
        locations.add(AntelliLocation("Cheb", "cheb", 50.081379, 12.373931))
        locations.add(AntelliLocation("Chomutov", "chomutov", 50.464279, 13.413223))
        locations.add(AntelliLocation("Chrudim", "chrudim", 49.950557, 15.796226))
        locations.add(AntelliLocation("Jablonec nad Nisou", "jablonec", 50.723416, 15.171722))
        locations.add(AntelliLocation("Jeseník", "jesenik", 50.225684, 17.200073))
        locations.add(AntelliLocation("Jičín", "jicin", 50.436297, 15.362610))
        locations.add(AntelliLocation("Jihlava", "jihlava", 49.399133, 15.585426))
        locations.add(AntelliLocation("Jindřichův Hradec", "jindrichuv-hradec", 49.146233, 15.008590))
        locations.add(AntelliLocation("Karlovy Vary", "karlovy-vary", 50.232054, 12.871460))
        locations.add(AntelliLocation("Karviná", "karvina", 49.857685, 18.544525))
        locations.add(AntelliLocation("Kladno", "kladno", 50.142806, 14.108452))
        locations.add(AntelliLocation("Klatovy", "klatovy", 49.397569, 13.299530))
        locations.add(AntelliLocation("Kolín", "kolin", 50.028255, 15.204681))
        locations.add(AntelliLocation("Kroměříž", "kromeriz", 49.293561, 17.401260))
        locations.add(AntelliLocation("Kutná Hora", "kutna-hora", 49.952987, 15.270599))
        locations.add(AntelliLocation("Liberec", "liberec", 50.768168, 15.054595))
        locations.add(AntelliLocation("Litoměřice", "litomerice", 50.539181, 14.131454))
        locations.add(AntelliLocation("Louny", "louny", 50.355538, 13.805298))
        locations.add(AntelliLocation("Mělník", "melnik", 50.355100, 14.483360))
        locations.add(AntelliLocation("Mladá Boleslav", "mlada-boleslav", 50.424050, 14.921783))
        locations.add(AntelliLocation("Most", "most", 50.503383, 13.636672))
        locations.add(AntelliLocation("Náchod", "nachod", 50.415519, 16.165642))
        locations.add(AntelliLocation("Nový Jičín", "novy-jicin", 49.587123, 18.031944))
        locations.add(AntelliLocation("Nymburk", "nymburk", 50.186242, 15.044376))
        locations.add(AntelliLocation("Olomouc", "olomouc", 49.596470, 17.255294))
        locations.add(AntelliLocation("Opava", "opava", 49.942383, 17.897995))
        locations.add(AntelliLocation("Ostrava", "ostrava", 49.822923, 18.266037))
        locations.add(AntelliLocation("Pardubice", "pardubice", 50.035974, 15.783813))
        locations.add(AntelliLocation("Pelhřimov", "pelhrimov", 49.432859, 15.226257))
        locations.add(AntelliLocation("Písek", "pisek", 49.304307, 14.159950))
        locations.add(AntelliLocation("Plzeň", "plzen", 49.740457, 13.377120))
        locations.add(AntelliLocation("Praha", "praha", 50.081820, 14.444060))
        locations.add(AntelliLocation("Prachatice", "prachatice", 49.011978, 14.002365))
        locations.add(AntelliLocation("Prostějov", "prostejov", 49.473255, 17.108406))
        locations.add(AntelliLocation("Přerov", "prerov", 49.457860, 17.452072))
        locations.add(AntelliLocation("Příbram", "pribram", 49.686734, 14.000305))
        locations.add(AntelliLocation("Rakovník", "rakovnik", 50.106488, 13.741096))
        locations.add(AntelliLocation("Rokycany", "rokycany", 49.738460, 13.594497))
        locations.add(AntelliLocation("Rychnov nad Kněžnou", "rychnov-nad-kneznou", 50.166563, 16.279625))
        locations.add(AntelliLocation("Semily", "semily", 50.605685, 15.329162))
        locations.add(AntelliLocation("Sokolov", "sokolov", 50.175359, 12.662033))
        locations.add(AntelliLocation("Strakonice", "strakonice", 49.259720, 13.907624))
        locations.add(AntelliLocation("Sušice", "susice", 49.232620, 13.522056))
        locations.add(AntelliLocation("Svitavy", "svitavy", 49.755431, 16.469852))
        locations.add(AntelliLocation("Šumperk", "sumperk", 49.978605, 16.973823))
        locations.add(AntelliLocation("Tábor", "tabor", 49.414324, 14.679397))
        locations.add(AntelliLocation("Tachov", "tachov", 49.800104, 12.639030))
        locations.add(AntelliLocation("Teplice", "teplice", 50.645324, 13.836883))
        locations.add(AntelliLocation("Trutnov", "trutnov", 50.567102, 15.912216))
        locations.add(AntelliLocation("Třebíč", "trebic", 49.215803, 15.882400))
        locations.add(AntelliLocation("Uherské Hradiště", "uherske-hradiste", 49.061045, 17.496704))
        locations.add(AntelliLocation("Ústí nad Labem", "usti-nad-labem", 50.663390, 14.056213))
        locations.add(AntelliLocation("Ústí nad Orlicí", "usti-nad-orlici", 49.971539, 16.400421))
        locations.add(AntelliLocation("Vsetín", "vsetin", 49.341231, 17.996871))
        locations.add(AntelliLocation("Vyškov", "vyskov", 49.279453, 16.999862))
        locations.add(AntelliLocation("Zlín", "zlin", 49.226118, 17.675521))
        locations.add(AntelliLocation("Znojmo", "znojmo", 48.857487, 16.059158))
        locations.add(AntelliLocation("Žďár nad Sázavou", "zdar-nad-sazavou", 49.564638, 15.940078))
    }

    private fun getNearestLocation(myLocation: Location?, locations: ArrayList<AntelliLocation>?): AntelliLocation? {
        var nearestIndex = -1
        var nearestDistance = java.lang.Double.POSITIVE_INFINITY

        if (myLocation != null) {
            for (i in locations!!.indices) {
                val distance = myLocation.distanceTo(locations[i])
                if (distance < nearestDistance) {
                    nearestDistance = distance.toDouble()
                    nearestIndex = i
                }
            }
            return locations[nearestIndex]
        } else {
            return null
        }
    }

    companion object {

        private val ACTION_FORECAST = "forecast"
        private val PARAM_SOURCE = "source"
        private val PARAM_DELTA = "delta"

        private fun daysBetween(startDate: Calendar, endDate: Calendar): Int {
            val date = startDate.clone() as Calendar
            var daysBetween = 0
            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1)
                daysBetween++
            }
            return daysBetween
        }
    }
}

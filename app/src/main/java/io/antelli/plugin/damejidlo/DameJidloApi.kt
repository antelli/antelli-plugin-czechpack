package io.antelli.plugin.damejidlo

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.damejidlo.entity.Restaurant
import io.antelli.plugin.damejidlo.entity.SearchResult
import io.reactivex.Observable
import okhttp3.*


class DameJidloApi : BaseWebApi() {


    fun search(query : String) : Observable<SearchResult>{
        val requestBody = FormBody.Builder()
                .add("address", "Svojšovická 2832/6")
                .add("_do", "localitySelection-form-submit")
                .add("send", "Zobrazit nabídku")
                .build()
        val request = Request.Builder().url("https://www.damejidlo.cz")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .addHeader("Host", "www.damejidlo.cz")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Referer", "https://www.damejidlo.cz")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36").build()

        return request(request = request)
                .flatMap {
                    request(Request.Builder().url("https://www.damejidlo.cz/vyhledavani?q=$query").build())
                }.map {
                    val restaurantsStart = it.indexOf("fake-h1")
                    val foodsStart = it.indexOf("fake-h1", restaurantsStart+1)
                    val restaurantsSrc = it.substring(restaurantsStart, foodsStart)

                    val restaurantsParsed = parse(restaurantsSrc, "restaurant-list__item\">.*?href=\"(.*?)\".*?img src=\"(.*?)\".*?_item-title \">(.*?)</h2>.*?<strong>(.*?)</strong>.*?time\">(.*?)</span>.*?minimal-order\">(.*?)</div.*?rating__text-top\">.*?<strong>(.*?)</strong>")
                    var restaurant : Restaurant
                    for (r in restaurantsParsed.items) {
                        restaurant = Restaurant()
                        restaurant.link = r[0]
                        restaurant.image = r[1]
                        restaurant.name = r[2]
                        restaurant.minimumOrder = r[3]
                        restaurant.time = r[4]
                        restaurant.rating = r[5]
                    }

                    val foodsSrc = it.substring(foodsStart)
                    val result = SearchResult()

                    return@map result
                }
    }

}
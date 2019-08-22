package io.antelli.plugin

import android.text.Html
import io.antelli.plugin.base.AntelliCookieJar
import io.antelli.plugin.base.ParsedResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

import java.util.ArrayList
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

abstract class BaseWebApi {

    var client: OkHttpClient? = null

    protected fun getHtmlFrom(url: String): Observable<String> {
        return Observable.defer(Callable<ObservableSource<Response>> {
            try {
                if (client == null){
                    client = OkHttpClient.Builder()
                            .cookieJar(AntelliCookieJar())
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build()
                }
                val response = client!!.newCall(Request.Builder().url(url).build()).execute()
                return@Callable Observable.just(response)
            } catch (e: IOException) {
                return@Callable Observable.error(e)
            }
        }).map { response -> response.body!!.string() }
    }

    protected fun request(request: Request): Observable<String> {
        return Observable.defer(Callable<ObservableSource<Response>> {
            try {
                if (client == null){
                    client = OkHttpClient.Builder()
                            .cookieJar(AntelliCookieJar())
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build()
                }
                val response = client!!.newCall(request).execute()
                return@Callable Observable.just(response)
            } catch (e: IOException) {
                return@Callable Observable.error(e)
            }
        }).map { response -> response.body!!.string() }
    }

    protected fun parse(html: String, regexp: String): ParsedResult {
        val result = ArrayList<List<String>>()
        val matcher = Pattern.compile(regexp, Pattern.DOTALL).matcher(html)

        while (matcher.find()) {
            val row = ArrayList<String>()
            for (i in 1 until matcher.groupCount() + 1) {
                row.add(matcher.group(i).trim { it <= ' ' })
            }
            result.add(row)
        }
        return ParsedResult(result)
    }

    protected fun removeHtmlTags(string: String): String {
        return Html.fromHtml(string).toString()
    }
}
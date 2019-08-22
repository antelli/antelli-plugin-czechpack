package io.antelli.plugin.csfd

import java.util.ArrayList

import io.antelli.plugin.BaseWebApi
import io.antelli.plugin.csfd.entity.Artist
import io.antelli.plugin.csfd.entity.ArtistGroup
import io.antelli.plugin.csfd.entity.Movie
import io.reactivex.Observable
import io.reactivex.functions.Function

class CsfdApi : BaseWebApi() {

    fun search(query: String): Observable<List<Movie>> {

        return getHtmlFrom(BASE_URL + "/hledat/?q=" + query.replace(" ", "+"))
                .map { html ->
                    val result = ArrayList<Movie>()
                    val parsedList = parse(html, "Filmy</h2>(.*?)</ul>")

                    if (!parsedList.isEmpty) {
                        val parsed = parse(parsedList.first(0), "<li>.*?src=\"(.*?)\".*?href=\"(.*?)\".*?>(.*?)</.*?<p>(.*?)</p>.*?<p>(.*?)</p>.*?</li>")

                        if (!parsed.isEmpty) {
                            for (row in parsed.items) {
                                val item = Movie()
                                item.name = row[2]
                                item.genre = row[3]
                                item.poster = if (row[0].contains("http")) row[0] else "http:" + row[0]
                                item.link = BASE_URL + row[1]
                                result.add(item)
                            }
                        }
                    }
                    result
                }

    }

    fun getMovieDetail(link: String): Observable<Movie> {
        return getHtmlFrom(link).map(object : Function<String, Movie> {
            @Throws(Exception::class)
            override fun apply(html: String): Movie? {

                var result: Movie? = null
                val parsed = parse(html, "class=\"content\"(.*?)class=\"info\".*?<h1.*?>(.*?)</h1>.*?<ul class=\"names\">(.*?)</ul>.*?genre\">(.*?)</.*?origin\">(.*?)</.*?class=\"creators\"(.*?)footer(.*?)average\">(.*?)</h2>")

                //0 - poster
                //1 - name
                //2 - names
                //3 - genre
                //4 - origin
                //5 - creators
                //6 - description
                //7 - rating


                if (!parsed.isEmpty) {
                    result = Movie()
                    result.link = link
                    result.poster = parsePoster(parsed.first(0))
                    result.name = removeHtmlTags(parsed.first(1))
                    val enName = parseEnName(parsed.first(2))
                    result.nameEn = enName ?: result.name
                    result.genre = parsed.first(3)
                    result.origin = removeHtmlTags(parsed.first(4))
                    result.artists = parseArtistGroups(parsed.first(5))
                    result.description = parseDescription(parsed.first(6))
                    result.rating = parsed.first(7)
                }
                return result
            }

            private fun parseArtistGroups(html: String): List<ArtistGroup> {

                val creatorSpans = parse(html, "<h4>(.*?)</h4>(.*?)</span>")
                val result = ArrayList<ArtistGroup>()

                for (row in creatorSpans.items) {
                    if (filterArtistGroup(row[0])) {
                        result.add(ArtistGroup(row[0], parseArtists(row[1])))
                    }
                }
                return result
            }

            private fun filterArtistGroup(name: String): Boolean {
                return name == "Režie:" || name == "Hudba:" || name == "Hrají:"
            }

            private fun parseArtists(html: String): List<Artist> {
                val result = ArrayList<Artist>()
                val parsed = parse(html, "<a href=\"(.*?)\">(.*?)</a>")
                for (row in parsed.items) {
                    result.add(Artist(row[1], BASE_URL + row[0]))
                }
                return result
            }
        })
    }

    private fun parsePoster(html: String): String {
        val parsed = parse(html, "src=\"(.*?)\"")
        return if (parsed.isEmpty) {
            "https://img.csfd.cz/assets/b87/images/poster-free.png"
        } else {
            if (parsed.first(0).startsWith("http")) parsed.first(0) else "https:" + parsed.first(0)
        }
    }

    private fun parseEnName(html: String): String? {
        val result = parse(html, "<li>.*?title=\"(.*?)\".*?<h3>(.*?)</h3>.*?</li>")
        for (row in result.items) {
            if (row[0] == "USA") {
                return row[1]
            }

        }
        return null
    }

    private fun parseDescription(html: String): String? {
        val parsed = parse(html, "Obsah.*?</h3>.*?<li>(.*?)</li>")
        return if (!parsed.isEmpty) {
            removeHtmlTags(parsed.first(0)).trim { it <= ' ' }
        } else null
    }

    companion object {

        private val BASE_URL = "https://www.csfd.cz"
    }
}

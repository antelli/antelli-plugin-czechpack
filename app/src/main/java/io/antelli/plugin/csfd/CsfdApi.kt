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
                    val parsedList = parse(html, "Filmy</h2>(.*?)</section>")

                    if (!parsedList.isEmpty) {
                        val parsed = parse(parsedList.first(0), "class=\"article-img\".*?src=\"(.*?)\".*?href=\"(.*?)\".*?film-title-name\">(.*?)</a.*?class=\"info\">(.*?)</.*?</article>")

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
                val parsed = parse(html, "<h1.*?>(.*?)</h1>.*?<ul class=\"film-names\">(.*?)</ul>.*?<div class=\"film-posters\">(.*?)</div>.*?rating-average\">(.*?)</div>.*?genres\">(.*?)</.*?origin\">(.*?)</div.*?class=\"creators\"(.*?)box-header(.*?)class=\"tabs\"")

                //0 - name
                //1 - names
                //2 - poster
                //3 - ranking
                //4 - genre
                //5 - origin
                //6 - creators
                //7 - description

                if (!parsed.isEmpty) {
                    result = Movie()
                    result.link = link
                    result.poster = parsePoster(parsed.first(2))
                    result.name = removeHtmlTags(parsed.first(0))
                    val enName = parseEnName(parsed.first(1))
                    result.nameEn = enName ?: result.name
                    result.genre = parsed.first(4)
                    result.origin = removeHtmlTags(parsed.first(5))
                    result.artists = parseArtistGroups(parsed.first(6))
                    result.description = parseDescription(parsed.first(7))
                    result.rating = parsed.first(3)
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
        val parsed = parse(html, "Obsah.*?</h3>.*?<p>(.*?)</p>")
        return if (!parsed.isEmpty) {
            removeHtmlTags(parsed.first(0)).trim { it <= ' ' }
        } else null
    }

    companion object {

        private val BASE_URL = "https://www.csfd.cz"
    }
}

package io.antelli.plugin.csfd

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.RemoteException
import android.provider.MediaStore

import io.antelli.plugin.BaseWebPlugin
import io.antelli.plugin.base.ErrorAnswer
import io.antelli.plugin.csfd.entity.Movie
import io.antelli.sdk.callback.IAnswerCallback
import io.antelli.sdk.callback.ICanAnswerCallback
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Hint
import io.antelli.sdk.model.Question
import io.reactivex.functions.Consumer

class CsfdPlugin : BaseWebPlugin<CsfdApi>() {

    @Throws(RemoteException::class)
    override fun canAnswer(question: Question, callback: ICanAnswerCallback) {
        callback.canAnswer(question.containsOne("film", "seriál", "kolik procent má"))
    }

    override fun answer(question: Question, callback: IAnswerCallback) {
        if (question.containsOne("hledej", "najdi", "hledat")) {
            api.search(removeKewords(question)!!)
                    .subscribe(Consumer { movies -> callback.answer(convert(movies)) }, error(callback))
        } else {
            api.search(removeKewords(question)!!)
                    .flatMap { movies ->
                        if (movies.size > 0) {
                            return@flatMap api.search(removeKewords(question)).flatMap { api.getMovieDetail(movies.get(0).getLink()) }
                        }
                        io.reactivex.Observable.empty<Movie>()
                    }
                    .subscribe(Consumer { movie -> callback.answer(convertDetail(movie)) },
                            error(callback))
        }
    }

    private fun error(callback: IAnswerCallback): Consumer<Throwable> {
        return Consumer { throwable -> callback.answer(ErrorAnswer()) }
    }

    private fun removeKewords(q: Question): String {
        return q.removeWords("najdi", "hledej", "hledat", "filmu", "film", "seriálu", "seriál", "hodnocení", "kolik procent má")
    }

    private fun convert(movies: List<Movie>?): Answer {
        val result = Answer()
        if (movies != null) {
            for (movie in movies) {
                result.addItem(AnswerItem().apply {
                    title = movie.name
                    subtitle = movie.genre
                    text = "\n\n\n"
                    speech = movie.name
                    image = movie.poster
                    type = AnswerItem.TYPE_CARD
                    command = Command(ACTION_MOVIE_DETAIL).apply {
                        putString(PARAM_LINK, movie.link)
                    }
                })
            }
        }
        return result
    }

    override fun initApi(): CsfdApi {
        return CsfdApi()
    }

    override fun command(command: Command, callback: IAnswerCallback) {
        when (command.action) {
            ACTION_MOVIE_DETAIL -> api.getMovieDetail(command.getString(PARAM_LINK) ?: "")
                    .subscribe(Consumer { movie -> callback.answer(convertDetail(movie)) }, error(callback))
        }
    }

    private fun convertDetail(movie: Movie): Answer {
        val result = Answer()

        //Headline item
        result.addItem(AnswerItem().apply {
            type = AnswerItem.TYPE_CARD
            title = movie.name
            subtitle = movie.genre
            text = movie.origin
            largeText = movie.rating
            image = movie.poster
            speech = movie.name + ". " + movie.rating + ". " + movie.genre + ". " + movie.description
            addHint(Hint("ČSFD", Command(getCsfdIntent(movie))))
            addHint(Hint("Trailer", Command(getTrailerIntent(movie))))
            addHint(Hint("Soundtrack", Command(getSoundtrackIntent(movie))))
        })

        if (movie.description != null) {
            result.addItem(AnswerItem().apply {
                type = AnswerItem.TYPE_CARD
                title = "Obsah"
                text = movie.description
            })
        }

        for (artists in movie.artists) {
            val artistsItem = AnswerItem().apply {
                type = AnswerItem.TYPE_CAROUSEL_SMALL
                title = artists.name
            }

            for (artist in artists.artists) {
                artistsItem.addItem(AnswerItem().apply {
                    title = artist.name
                    image = "https://img.csfd.cz/assets/b87/images/poster-free.png"
                    command = Command(Intent(Intent.ACTION_VIEW, Uri.parse(artist.link)))
                })
                //Log.d("img", artistsItem.getImage());
            }

            result.addItem(artistsItem)
        }

        return result
    }

    private fun getSoundtrackIntent(movie: Movie): Intent {
        val ostIntent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
        ostIntent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, movie.musicArtist)
        ostIntent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE)
        ostIntent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM, movie.nameEn)
        ostIntent.putExtra(SearchManager.QUERY, movie.nameEn + " " + movie.musicArtist)
        return ostIntent
    }

    private fun getTrailerIntent(movie: Movie): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + movie.nameEn.replace(" ", "+") + "+trailer"))
    }

    private fun getCsfdIntent(movie: Movie): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(movie.link))
    }

    companion object {

        private val ACTION_MOVIE_DETAIL = "MOVIE_DETAIL"
        private val PARAM_LINK = "LINK"
    }
}

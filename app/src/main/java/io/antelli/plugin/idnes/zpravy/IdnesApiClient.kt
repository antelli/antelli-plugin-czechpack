package io.antelli.plugin.idnes.zpravy

import android.text.Html

import java.util.ArrayList
import java.util.HashMap

import io.antelli.plugin.BaseRestApi
import io.antelli.plugin.idnes.zpravy.response.article.ArticleResponse
import io.antelli.plugin.idnes.zpravy.response.entity.Article
import io.antelli.plugin.idnes.zpravy.response.entity.ArticleList
import io.antelli.plugin.idnes.zpravy.response.articlelist.ArticleListResponse
import io.antelli.plugin.idnes.zpravy.response.entity.Photo
import io.antelli.plugin.idnes.zpravy.response.section.Section
import io.antelli.plugin.idnes.zpravy.response.section.SectionResponse
import io.antelli.sdk.model.Answer
import io.antelli.sdk.model.AnswerItem
import io.antelli.sdk.model.Command
import io.antelli.sdk.model.Question
import io.antelli.sdk.model.Hint
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.lang.Exception
import kotlin.reflect.KClass

/**
 * Handcrafted by Štěpán Šonský on 10.11.2017.
 */

class IdnesApiClient : BaseRestApi<IdnesApiDef>() {
    override val apiDefClass : KClass<IdnesApiDef>
        get() = IdnesApiDef::class

    private val articleCache = HashMap<String?, Article>()
    private val sectionMgr = SectionManager()

    override val baseUrl: String
        get() = "https://servis.idnes.cz/ExportApi/"


    private fun getSphArticleList(): Observable<ArticleList> {
        return api.articlelistSph().map { articleListResponse ->
            for (article in articleListResponse.result.articleList.articles) {
                articleCache[article.idArticle] = article
            }
            articleListResponse.result.articleList
        }
    }

    internal fun getSections(id: String?): Observable<List<Section>> {
        return api.section(id ?: "idnes").map {
            it.result.section.sections
        }
    }

    fun getArticles(sectionId: String?): Observable<Answer> {
        return Observable.zip(getArticleList(sectionId), getSections(sectionId), BiFunction { articleList, sections ->
            val answer = Answer()
            for (article in articleList.getArticles()) {
                var img: String? = null

                if (article.getPhoto() != null &&
                        article.getPhoto().getTypes() != null && article.getPhoto().getTypes().size > 0) {
                    img = article.getPhoto().getTypes().get(0).getUrl()
                }
                if (article.getTitle() != null) {
                    answer.addItem(AnswerItem().apply {
                        title = article.getTitle()
                        image = img
                        type = AnswerItem.TYPE_CARD
                        command = Command(IdnesNewsPlugin.ACTION_ARTICLE).apply {
                            putString(IdnesNewsPlugin.PARAM_ID, article.getIdArticle())
                        }
                        speech = article.getTitle()
                    })
                }
            }

            val lastItem = answer.items[answer.items.size - 1]
            for (sect in sections) {
                lastItem.addHint(Hint(sect.namePublic, Command(IdnesNewsPlugin.ACTION_SECTION).apply {
                    putString(IdnesNewsPlugin.PARAM_ID, sect.idSection)
                }))
            }

            answer
        })
    }

    fun getArticles(question: Question): Observable<Answer> {
        val section = sectionMgr.getSectionFrom(question)
        return getArticles(section)
    }

    fun loadArticle(idArticle: String): Observable<Article> {
        return api.article(idArticle).map { articleResponse -> articleResponse.result.article }
    }

    fun getArticle(idArticle: String): Observable<Answer> {

        val source: Observable<Article>
        if (articleCache.containsKey(idArticle)) {
            source = Observable.just(articleCache[idArticle]!!)
        } else {
            source = loadArticle(idArticle)
        }

        return source.map { article ->
            val text = Html.fromHtml(article.textMobile.replace("@import.*?\\);".toRegex(), "")).toString()

            val answer = Answer()

            if (article.photoGallery != null && article.photoGallery.count > 0) {

                val gallery = ArrayList<AnswerItem>()

                for (photo in article.photoGallery.photo) {
                    val title = photo.textShort
                    val source1 = photo.source.name

                    gallery.add(AnswerItem().apply {
                        this.title = title
                        this.subtitle = source1
                        image = photo.types[0].url
                    })
                }

                answer.addItem(AnswerItem().apply {
                    title = article.title
                    type = AnswerItem.TYPE_CONVERSATION
                    items = gallery
                })

                answer.addItem(AnswerItem().apply {
                    this.text = text
                    type = AnswerItem.TYPE_CARD
                    speech = text
                })
            } else {
                answer.addItem(AnswerItem().apply {
                    title = article.title
                    this.text = text
                    type = AnswerItem.TYPE_CARD
                    speech = text
                })
            }

            answer
        }
    }

    private fun getArticleList(id: String?): Observable<ArticleList> {
        return if (id == null) {
            getSphArticleList()
        } else {
            api.articlelistByTime(id, 1, 4).map { articleListResponse ->
                for (article in articleListResponse.result.articleList.articles) {
                    articleCache[article.idArticle] = article
                }
                articleListResponse.result.articleList
            }
        }
    }
}

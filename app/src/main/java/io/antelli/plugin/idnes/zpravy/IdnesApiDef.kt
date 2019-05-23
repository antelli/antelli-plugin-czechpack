package io.antelli.plugin.idnes.zpravy

import io.antelli.plugin.idnes.zpravy.response.article.ArticleResponse
import io.antelli.plugin.idnes.zpravy.response.articlelist.ArticleListResponse
import io.antelli.plugin.idnes.zpravy.response.section.SectionResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Handcrafted by Štěpán Šonský on 10.11.2017.
 */

interface IdnesApiDef {

    @GET("idnes.aspx?t=articlelistbytime")
    fun articlelistByTime(@Query("id") id: String, @Query("page") page: Int, @Query("onpage") onpage: Int): Observable<ArticleListResponse>

    @GET("idnes.aspx?t=articlelistsph&id=all")
    fun articlelistSph(): Observable<ArticleListResponse>

    @GET("idnes.aspx?t=section")
    fun section(@Query("id") id: String): Observable<SectionResponse>

    @GET("idnes.aspx?t=article")
    fun article(@Query("id") id: String): Observable<ArticleResponse>

}
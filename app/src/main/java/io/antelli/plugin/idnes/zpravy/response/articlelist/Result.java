
package io.antelli.plugin.idnes.zpravy.response.articlelist;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import io.antelli.plugin.idnes.zpravy.response.entity.ArticleList;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("articleList")
    private ArticleList mArticleList;
    @SerializedName("page")
    private Long mPage;

    public ArticleList getArticleList() {
        return mArticleList;
    }

    public void setArticleList(ArticleList articleList) {
        mArticleList = articleList;
    }

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

}

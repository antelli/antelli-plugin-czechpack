
package io.antelli.plugin.idnes.zpravy.response.article;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import io.antelli.plugin.idnes.zpravy.response.entity.Article;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("article")
    private Article mArticle;

    public Article getArticle() {
        return mArticle;
    }

    public void setArticle(Article article) {
        mArticle = article;
    }

}

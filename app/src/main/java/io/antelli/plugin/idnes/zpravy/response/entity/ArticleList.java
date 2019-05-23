
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ArticleList {

    @SerializedName("articles")
    private List<Article> mArticles;
    @SerializedName("countAll")
    private Long mCountAll;

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }

    public Long getCountAll() {
        return mCountAll;
    }

    public void setCountAll(Long countAll) {
        mCountAll = countAll;
    }

}

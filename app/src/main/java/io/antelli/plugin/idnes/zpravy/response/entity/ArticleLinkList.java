
package io.antelli.plugin.idnes.zpravy.response.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ArticleLinkList {

    @SerializedName("idArticle")
    private String mIdArticle;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("url")
    private String mUrl;

    public String getIdArticle() {
        return mIdArticle;
    }

    public void setIdArticle(String idArticle) {
        mIdArticle = idArticle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}

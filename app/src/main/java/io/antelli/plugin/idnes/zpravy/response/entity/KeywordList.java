
package io.antelli.plugin.idnes.zpravy.response.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class KeywordList {

    @SerializedName("externalLink")
    private String mExternalLink;
    @SerializedName("idKeyword")
    private Long mIdKeyword;
    @SerializedName("partLink")
    private String mPartLink;
    @SerializedName("showInArticle")
    private Boolean mShowInArticle;
    @SerializedName("word")
    private String mWord;

    public String getExternalLink() {
        return mExternalLink;
    }

    public void setExternalLink(String externalLink) {
        mExternalLink = externalLink;
    }

    public Long getIdKeyword() {
        return mIdKeyword;
    }

    public void setIdKeyword(Long idKeyword) {
        mIdKeyword = idKeyword;
    }

    public String getPartLink() {
        return mPartLink;
    }

    public void setPartLink(String partLink) {
        mPartLink = partLink;
    }

    public Boolean getShowInArticle() {
        return mShowInArticle;
    }

    public void setShowInArticle(Boolean showInArticle) {
        mShowInArticle = showInArticle;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

}

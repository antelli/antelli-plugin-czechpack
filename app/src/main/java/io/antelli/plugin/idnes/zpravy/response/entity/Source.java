
package io.antelli.plugin.idnes.zpravy.response.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Source {

    @SerializedName("isShownNearAuthor")
    private Boolean mIsShownNearAuthor;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;

    public Boolean getIsShownNearAuthor() {
        return mIsShownNearAuthor;
    }

    public void setIsShownNearAuthor(Boolean isShownNearAuthor) {
        mIsShownNearAuthor = isShownNearAuthor;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}

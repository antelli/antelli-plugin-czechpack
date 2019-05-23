
package io.antelli.plugin.idnes.zpravy.response.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ArticleSource {

    @SerializedName("appearance")
    private String mAppearance;
    @SerializedName("isShownNearAuthor")
    private Boolean mIsShownNearAuthor;
    @SerializedName("name")
    private String mName;

    public String getAppearance() {
        return mAppearance;
    }

    public void setAppearance(String appearance) {
        mAppearance = appearance;
    }

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

}

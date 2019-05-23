
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PhotoGallery {

    @SerializedName("articleTitle")
    private String mArticleTitle;
    @SerializedName("count")
    private Long mCount;
    @SerializedName("photo")
    private List<Photo> mPhoto;

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        mArticleTitle = articleTitle;
    }

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public List<Photo> getPhoto() {
        return mPhoto;
    }

    public void setPhoto(List<Photo> photo) {
        mPhoto = photo;
    }

}


package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Video {

    @SerializedName("bannerVideoAd")
    private List<Object> mBannerVideoAd;
    @SerializedName("idVideo")
    private String mIdVideo;
    @SerializedName("mobileAuthors")
    private String mMobileAuthors;
    @SerializedName("photo")
    private Photo mPhoto;
    @SerializedName("section")
    private Section mSection;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("type")
    private String mType;
    @SerializedName("videoStream")
    private String mVideoStream;
    @SerializedName("videoStreamHQ")
    private String mVideoStreamHQ;
    @SerializedName("videoStreamXmlUrl")
    private String mVideoStreamXmlUrl;

    public List<Object> getBannerVideoAd() {
        return mBannerVideoAd;
    }

    public void setBannerVideoAd(List<Object> bannerVideoAd) {
        mBannerVideoAd = bannerVideoAd;
    }

    public String getIdVideo() {
        return mIdVideo;
    }

    public void setIdVideo(String idVideo) {
        mIdVideo = idVideo;
    }

    public String getMobileAuthors() {
        return mMobileAuthors;
    }

    public void setMobileAuthors(String mobileAuthors) {
        mMobileAuthors = mobileAuthors;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public Section getSection() {
        return mSection;
    }

    public void setSection(Section section) {
        mSection = section;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getVideoStream() {
        return mVideoStream;
    }

    public void setVideoStream(String videoStream) {
        mVideoStream = videoStream;
    }

    public String getVideoStreamHQ() {
        return mVideoStreamHQ;
    }

    public void setVideoStreamHQ(String videoStreamHQ) {
        mVideoStreamHQ = videoStreamHQ;
    }

    public String getVideoStreamXmlUrl() {
        return mVideoStreamXmlUrl;
    }

    public void setVideoStreamXmlUrl(String videoStreamXmlUrl) {
        mVideoStreamXmlUrl = videoStreamXmlUrl;
    }

}

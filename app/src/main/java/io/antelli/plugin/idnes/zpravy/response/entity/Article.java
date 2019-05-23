
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Article {

    @SerializedName("articleLinkList")
    private List<ArticleLinkList> mArticleLinkList;
    @SerializedName("articleSources")
    private List<ArticleSource> mArticleSources;
    @SerializedName("audioGallery")
    private AudioGallery mAudioGallery;
    @SerializedName("discussionUrl")
    private String mDiscussionUrl;
    @SerializedName("discussionVisible")
    private Boolean mDiscussionVisible;
    @SerializedName("foreignAuthor")
    private String mForeignAuthor;
    @SerializedName("foreignSource")
    private Boolean mForeignSource;
    @SerializedName("idArticle")
    private String mIdArticle;
    @SerializedName("idDiscussion")
    private String mIdDiscussion;
    @SerializedName("idTemplateXSLT")
    private String mIdTemplateXSLT;
    @SerializedName("keywordList")
    private List<KeywordList> mKeywordList;
    @SerializedName("lead")
    private String mLead;
    @SerializedName("mobileAuthors")
    private String mMobileAuthors;
    @SerializedName("mobileUrl")
    private String mMobileUrl;
    @SerializedName("photo")
    private Photo mPhoto;
    @SerializedName("photoGallery")
    private PhotoGallery mPhotoGallery;
    @SerializedName("portalSection")
    private PortalSection mPortalSection;
    @SerializedName("section")
    private Section mSection;
    @SerializedName("textMobile")
    private String mTextMobile;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("titleSEO")
    private String mTitleSEO;
    @SerializedName("updated")
    private String mUpdated;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("videoGallery")
    private VideoGallery mVideoGallery;

    public List<ArticleLinkList> getArticleLinkList() {
        return mArticleLinkList;
    }

    public void setArticleLinkList(List<ArticleLinkList> articleLinkList) {
        mArticleLinkList = articleLinkList;
    }

    public List<ArticleSource> getArticleSources() {
        return mArticleSources;
    }

    public void setArticleSources(List<ArticleSource> articleSources) {
        mArticleSources = articleSources;
    }

    public AudioGallery getAudioGallery() {
        return mAudioGallery;
    }

    public void setAudioGallery(AudioGallery audioGallery) {
        mAudioGallery = audioGallery;
    }

    public String getDiscussionUrl() {
        return mDiscussionUrl;
    }

    public void setDiscussionUrl(String discussionUrl) {
        mDiscussionUrl = discussionUrl;
    }

    public Boolean getDiscussionVisible() {
        return mDiscussionVisible;
    }

    public void setDiscussionVisible(Boolean discussionVisible) {
        mDiscussionVisible = discussionVisible;
    }

    public String getForeignAuthor() {
        return mForeignAuthor;
    }

    public void setForeignAuthor(String foreignAuthor) {
        mForeignAuthor = foreignAuthor;
    }

    public Boolean getForeignSource() {
        return mForeignSource;
    }

    public void setForeignSource(Boolean foreignSource) {
        mForeignSource = foreignSource;
    }

    public String getIdArticle() {
        return mIdArticle;
    }

    public void setIdArticle(String idArticle) {
        mIdArticle = idArticle;
    }

    public String getIdDiscussion() {
        return mIdDiscussion;
    }

    public void setIdDiscussion(String idDiscussion) {
        mIdDiscussion = idDiscussion;
    }

    public String getIdTemplateXSLT() {
        return mIdTemplateXSLT;
    }

    public void setIdTemplateXSLT(String idTemplateXSLT) {
        mIdTemplateXSLT = idTemplateXSLT;
    }

    public List<KeywordList> getKeywordList() {
        return mKeywordList;
    }

    public void setKeywordList(List<KeywordList> keywordList) {
        mKeywordList = keywordList;
    }

    public String getLead() {
        return mLead;
    }

    public void setLead(String lead) {
        mLead = lead;
    }

    public String getMobileAuthors() {
        return mMobileAuthors;
    }

    public void setMobileAuthors(String mobileAuthors) {
        mMobileAuthors = mobileAuthors;
    }

    public String getMobileUrl() {
        return mMobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        mMobileUrl = mobileUrl;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public PhotoGallery getPhotoGallery() {
        return mPhotoGallery;
    }

    public void setPhotoGallery(PhotoGallery photoGallery) {
        mPhotoGallery = photoGallery;
    }

    public PortalSection getPortalSection() {
        return mPortalSection;
    }

    public void setPortalSection(PortalSection portalSection) {
        mPortalSection = portalSection;
    }

    public Section getSection() {
        return mSection;
    }

    public void setSection(Section section) {
        mSection = section;
    }

    public String getTextMobile() {
        return mTextMobile;
    }

    public void setTextMobile(String textMobile) {
        mTextMobile = textMobile;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitleSEO() {
        return mTitleSEO;
    }

    public void setTitleSEO(String titleSEO) {
        mTitleSEO = titleSEO;
    }

    public String getUpdated() {
        return mUpdated;
    }

    public void setUpdated(String updated) {
        mUpdated = updated;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public VideoGallery getVideoGallery() {
        return mVideoGallery;
    }

    public void setVideoGallery(VideoGallery videoGallery) {
        mVideoGallery = videoGallery;
    }

}

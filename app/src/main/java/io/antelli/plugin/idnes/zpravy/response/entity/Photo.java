
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Photo {

    @SerializedName("dateIns")
    private String mDateIns;
    @SerializedName("foreignAuthor")
    private String mForeignAuthor;
    @SerializedName("id")
    private String mId;
    @SerializedName("journalist")
    private Journalist mJournalist;
    @SerializedName("mobileAuthors")
    private String mMobileAuthors;
    @SerializedName("section")
    private Section mSection;
    @SerializedName("source")
    private Source mSource;
    @SerializedName("textLong")
    private String mTextLong;
    @SerializedName("textShort")
    private String mTextShort;
    @SerializedName("types")
    private List<Type> mTypes;

    public String getDateIns() {
        return mDateIns;
    }

    public void setDateIns(String dateIns) {
        mDateIns = dateIns;
    }

    public String getForeignAuthor() {
        return mForeignAuthor;
    }

    public void setForeignAuthor(String foreignAuthor) {
        mForeignAuthor = foreignAuthor;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Journalist getJournalist() {
        return mJournalist;
    }

    public void setJournalist(Journalist journalist) {
        mJournalist = journalist;
    }

    public String getMobileAuthors() {
        return mMobileAuthors;
    }

    public void setMobileAuthors(String mobileAuthors) {
        mMobileAuthors = mobileAuthors;
    }

    public Section getSection() {
        return mSection;
    }

    public void setSection(Section section) {
        mSection = section;
    }

    public Source getSource() {
        return mSource;
    }

    public void setSource(Source source) {
        mSource = source;
    }

    public String getTextLong() {
        return mTextLong;
    }

    public void setTextLong(String textLong) {
        mTextLong = textLong;
    }

    public String getTextShort() {
        return mTextShort;
    }

    public void setTextShort(String textShort) {
        mTextShort = textShort;
    }

    public List<Type> getTypes() {
        return mTypes;
    }

    public void setTypes(List<Type> types) {
        mTypes = types;
    }

}

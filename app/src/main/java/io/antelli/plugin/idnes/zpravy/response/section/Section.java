
package io.antelli.plugin.idnes.zpravy.response.section;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Section {

    @SerializedName("idSection")
    private String mIdSection;
    @SerializedName("namePublic")
    private String mNamePublic;
    @SerializedName("order")
    private Long mOrder;
    @SerializedName("sections")
    private List<Section> mSections;
    @SerializedName("views")
    private Long mViews;

    public String getIdSection() {
        return mIdSection;
    }

    public void setIdSection(String idSection) {
        mIdSection = idSection;
    }

    public String getNamePublic() {
        return mNamePublic;
    }

    public void setNamePublic(String namePublic) {
        mNamePublic = namePublic;
    }

    public Long getOrder() {
        return mOrder;
    }

    public void setOrder(Long order) {
        mOrder = order;
    }

    public List<Section> getSections() {
        return mSections;
    }

    public void setSections(List<Section> sections) {
        mSections = sections;
    }

    public Long getViews() {
        return mViews;
    }

    public void setViews(Long views) {
        mViews = views;
    }

}

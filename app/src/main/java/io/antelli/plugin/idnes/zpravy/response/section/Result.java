
package io.antelli.plugin.idnes.zpravy.response.section;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("section")
    private Section mSection;

    public Section getSection() {
        return mSection;
    }

    public void setSection(Section section) {
        mSection = section;
    }

}

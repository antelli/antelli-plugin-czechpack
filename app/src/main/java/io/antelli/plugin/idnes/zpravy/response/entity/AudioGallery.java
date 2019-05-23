
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AudioGallery {

    @SerializedName("audios")
    private List<Object> mAudios;

    public List<Object> getAudios() {
        return mAudios;
    }

    public void setAudios(List<Object> audios) {
        mAudios = audios;
    }

}

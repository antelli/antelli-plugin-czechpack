
package io.antelli.plugin.idnes.zpravy.response.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class VideoGallery {

    @SerializedName("count")
    private Long mCount;
    @SerializedName("video")
    private List<Video> mVideo;

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public List<Video> getVideo() {
        return mVideo;
    }

    public void setVideo(List<Video> video) {
        mVideo = video;
    }

}

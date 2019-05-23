
package io.antelli.plugin.idnes.zpravy.response.section;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SectionResponse {

    @SerializedName("result")
    private Result mResult;
    @SerializedName("status")
    private Long mStatus;

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}

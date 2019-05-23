package io.antelli.plugin.idnes.tv;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import framework.viewmodel.RecyclerItem;
import io.antelli.plugin.czechpack.R;

/**
 * Handcrafted by Štěpán Šonský on 5.9.16.
 */
public class IdnesTvChannel extends BaseObservable implements Parcelable, RecyclerItem {

    private int id;
    private String name;
    private String logo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
    }

    public IdnesTvChannel() {
    }

    protected IdnesTvChannel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<IdnesTvChannel> CREATOR = new Parcelable.Creator<IdnesTvChannel>() {
        @Override
        public IdnesTvChannel createFromParcel(Parcel source) {
            return new IdnesTvChannel(source);
        }

        @Override
        public IdnesTvChannel[] newArray(int size) {
            return new IdnesTvChannel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return "http:"+logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_idnes_tv_channel;
    }
}

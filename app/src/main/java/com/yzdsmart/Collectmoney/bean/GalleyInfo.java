package com.yzdsmart.Collectmoney.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YZD on 2016/9/21.
 */

public class GalleyInfo implements Parcelable {
    private Integer FileId;//文件编号
    private String ImageFileUrl;//图片url

    public GalleyInfo() {
    }

    public GalleyInfo(Integer fileId, String imageFileUrl) {
        FileId = fileId;
        ImageFileUrl = imageFileUrl;
    }

    protected GalleyInfo(Parcel in) {
        FileId = in.readInt();
        ImageFileUrl = in.readString();
    }

    public static final Creator<GalleyInfo> CREATOR = new Creator<GalleyInfo>() {
        @Override
        public GalleyInfo createFromParcel(Parcel in) {
            return new GalleyInfo(in);
        }

        @Override
        public GalleyInfo[] newArray(int size) {
            return new GalleyInfo[size];
        }
    };

    public Integer getFileId() {
        return FileId;
    }

    public void setFileId(Integer fileId) {
        FileId = fileId;
    }

    public String getImageFileUrl() {
        return ImageFileUrl;
    }

    public void setImageFileUrl(String imageFileUrl) {
        ImageFileUrl = imageFileUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "FileId:" + FileId +
                ", ImageFileUrl:'" + ImageFileUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(FileId);
        dest.writeString(ImageFileUrl);
    }
}

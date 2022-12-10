package com.example.wip.data.records;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagePartyRecord implements Parcelable {

    private int id;
    private String imagePath;
    private int idParty;

    public ImagePartyRecord(int id, String imagePath, int idParty) {
        this.id = id;
        this.imagePath = imagePath;
        this.idParty = idParty;
    }

    protected ImagePartyRecord(Parcel in) {
        id = in.readInt();
        imagePath = in.readString();
        idParty = in.readInt();
    }

    public static final Creator<ImagePartyRecord> CREATOR = new Creator<ImagePartyRecord>() {
        @Override
        public ImagePartyRecord createFromParcel(Parcel in) {
            return new ImagePartyRecord(in);
        }

        @Override
        public ImagePartyRecord[] newArray(int size) {
            return new ImagePartyRecord[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIdParty() {
        return idParty;
    }

    public void setIdParty(int idParty) {
        this.idParty = idParty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeInt(idParty);
    }
}


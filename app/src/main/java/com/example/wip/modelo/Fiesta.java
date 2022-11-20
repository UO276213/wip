package com.example.wip.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Fiesta implements Parcelable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name="";
    private String date="";
    private String place="";
    private String details="";
    private String townURL="";
    private boolean isFavorite;

    protected Fiesta(Parcel in) {
        name = in.readString();
        date = in.readString();
        place = in.readString();
        details= in.readString();
        townURL= in.readString();
    }

    public Fiesta() {
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(place);
        dest.writeString(details);
        dest.writeString(townURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Fiesta> CREATOR = new Creator<Fiesta>() {
        @Override
        public Fiesta createFromParcel(Parcel in) {
            return new Fiesta(in);
        }

        @Override
        public Fiesta[] newArray(int size) {
            return new Fiesta[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public String getTownURL() {
        return townURL;
    }

    public void setTownURL(String townURL) {
        this.townURL = townURL;
    }

    @NonNull
    @Override
    public String toString() {
        return place+"\n\t"+name+"\n\t\t"+date+"\n";
    }
}

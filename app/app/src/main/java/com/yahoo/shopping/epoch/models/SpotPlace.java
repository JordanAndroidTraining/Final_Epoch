package com.yahoo.shopping.epoch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jamesyan on 8/28/15.
 */
public class SpotPlace implements Parcelable{
    private String title;
    private String address;
    private String imageUrl;
    private String feature;
    private String reminder;
    private String trafficInfo;
    private String phoneNumber;

    public SpotPlace(String title, String address, String imageUrl, String feature, String reminder, String trafficInfo, String phoneNumber) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.address = address;
        this.feature = feature;
        this.reminder = reminder;
        this.trafficInfo = trafficInfo;
        this.phoneNumber = phoneNumber;
    }

    protected SpotPlace(Parcel in) {
        title = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        feature = in.readString();
        reminder = in.readString();
        trafficInfo = in.readString();
        phoneNumber = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static final Creator<SpotPlace> CREATOR = new Creator<SpotPlace>() {
        @Override
        public SpotPlace createFromParcel(Parcel in) {
            return new SpotPlace(in);
        }

        @Override
        public SpotPlace[] newArray(int size) {
            return new SpotPlace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeString(feature);
        dest.writeString(reminder);
        dest.writeString(trafficInfo);
        dest.writeString(phoneNumber);
    }
}

package com.yahoo.shopping.epoch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpotPlace implements Parcelable{
    private int rating;
    private int resourceId;
    private String title;
    private String address;
    private String imageUrl;
    private String feature;
    private String reminder;
    private String trafficInfo;
    private String phoneNumber;

    public SpotPlace(String title, String address, String imageUrl, String feature, String reminder, String trafficInfo, String phoneNumber, int rating,int resourceId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.address = address;
        this.feature = feature;
        this.reminder = reminder;
        this.trafficInfo = trafficInfo;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.resourceId = resourceId;
    }

    protected SpotPlace(Parcel in) {
        resourceId = in.readInt();
        rating = in.readInt();
        title = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        feature = in.readString();
        reminder = in.readString();
        trafficInfo = in.readString();
        phoneNumber = in.readString();
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public static SpotPlace parseFromJSONObject(JSONObject data){
        String title = data.optString("title","");
        String address = data.optString("address","");
        String imageUrl = data.optString("imageUrl","");
        String feature = data.optString("feature","");
        String reminder = data.optString("reminder","");
        String trafficInfo = data.optString("trafficInfo","");
        String phoneNumber = data.optString("phoneNumber","");
        int rating = data.optInt("averageRating", 0);
        int resourceId = data.optInt("id",0);

        SpotPlace spot = new SpotPlace(title,address,imageUrl,feature,reminder,trafficInfo,phoneNumber,rating,resourceId);
        return spot;
    }

    public static ArrayList<SpotPlace> parseFromJSONArray(JSONArray data){
        ArrayList<SpotPlace> returnList = new ArrayList<>();
        for(int i = 0; i < data.length() ; i++) {
            JSONObject row = data.optJSONObject(i);
            SpotPlace spot = SpotPlace.parseFromJSONObject(row);
            returnList.add(spot);
        }
        return  returnList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resourceId);
        dest.writeInt(rating);
        dest.writeString(title);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeString(feature);
        dest.writeString(reminder);
        dest.writeString(trafficInfo);
        dest.writeString(phoneNumber);
    }

}

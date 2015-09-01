package com.yahoo.shopping.epoch.models;

import com.yahoo.shopping.epoch.utils.GoogleImageResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpotPhoto implements Serializable {
    public String title;
    public String tbUrl;
    public int tbWidth;
    public int tbHeight;
    public String photoUrl;
    public int photoWidth;
    public int photoHeight;

    public SpotPhoto(GoogleImageResult imageResult) {
        this.title = imageResult.titleNoFormatting;
        this.tbUrl = imageResult.tbUrl;
        this.tbWidth = imageResult.tbWidth;
        this.tbHeight = imageResult.tbHeight;
        this.photoUrl = imageResult.url;
        this.photoWidth = imageResult.width;
        this.photoHeight = imageResult.height;
    }

    public static List<SpotPhoto> fromGoogleImageResults(List<GoogleImageResult> list) {
        List<SpotPhoto> aList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SpotPhoto photo = new SpotPhoto(list.get(i));
            aList.add(photo);
        }
        return aList;
    }
}

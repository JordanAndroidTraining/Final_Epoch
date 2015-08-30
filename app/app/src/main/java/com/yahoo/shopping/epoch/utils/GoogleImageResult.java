package com.yahoo.shopping.epoch.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleImageResult {
    public String title;
    public String content;
    public String titleNoFormatting;
    public String contentNoFormatting;
    // large image
    public String url;
    public int width;
    public int height;
    // small image
    public String tbUrl;
    public int tbWidth;
    public int tbHeight;

    public GoogleImageResult(JSONObject obj) {
        try {
            // title
            title = obj.getString("title");
            titleNoFormatting = obj.getString("titleNoFormatting");
            // content
            content = obj.getString("content");
            contentNoFormatting = obj.getString("contentNoFormatting");
            // large image
            url = obj.getString("url");
            width = obj.getInt("width");
            height = obj.getInt("height");
            // small image
            tbUrl = obj.getString("tbUrl");
            tbWidth = obj.getInt("tbWidth");
            tbHeight = obj.getInt("tbHeight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<GoogleImageResult> fromJSONArray(JSONArray list) {
        ArrayList<GoogleImageResult> aList = new ArrayList<>();
        for (int i = 0; i < list.length(); i++) {
            try {
                JSONObject obj = list.getJSONObject(i);
                // decode into model
                GoogleImageResult img = new GoogleImageResult(obj);
                // add to list
                aList.add(img);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return aList;
    }
}

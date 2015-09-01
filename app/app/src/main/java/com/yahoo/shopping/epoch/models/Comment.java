package com.yahoo.shopping.epoch.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jordanhsu on 9/2/15.
 */
public class Comment implements Serializable {
    private int commentId = 0;
    private String subject = "";
    private String comment = "";
    private String imageUrl = "";
    private int rating = 5;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public static Comment parseFromJSONObject(JSONObject data){
        Comment comment = new Comment();
        comment.setCommentId(data.optInt("id"));
        comment.setSubject(data.optString("subject"));
        comment.setComment(data.optString("comment"));
        comment.setImageUrl(data.optString("imageUrl"));
        comment.setRating(data.optInt("rating"));
        return comment;
    }

    public static ArrayList<Comment> parseFromJSONArray(JSONArray data){
        ArrayList<Comment> returnList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++){
            JSONObject row = data.optJSONObject(i);
            Comment comment = Comment.parseFromJSONObject(row);
            returnList.add(comment);
        }
        return  returnList;

    }
}

package com.yahoo.shopping.spotplace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by jamesyan on 8/28/15.
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String subject;
    private String comment;
    private String imageUrl;
    private int rating;

    public Comment() {

    }

    public Comment(String subject, String comment, String imageUrl) {
        this.subject = subject;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.rating = 0;
    }

    public Comment(String subject, String comment, String imageUrl, int rating) {
        this.subject = subject;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}

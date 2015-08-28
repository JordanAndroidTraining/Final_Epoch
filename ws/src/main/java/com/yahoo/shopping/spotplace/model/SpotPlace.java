package com.yahoo.shopping.spotplace.model;


import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by jamesyan on 8/27/15.
 */
@Entity
public class SpotPlace {
    public enum Fields {
        Title, Address, PhoneNumber, Feature, TrafficInfo, Reminder
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private SpotPlaceType type;
    private String title;
    private String address;
    private String phoneNumber;

    @Column(length = 2048)
    private String feature;

    @Column(length = 2048)
    private String trafficInfo;

    @Column(length = 2048)
    private String reminder;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;

    public SpotPlace() {
    }

    public SpotPlace(SpotPlaceType type, String title, String address, String phoneNumber, String feature, String trafficInfo, String reminder) {
        this.type = type;
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.feature = feature;
        this.trafficInfo = trafficInfo;
        this.reminder = reminder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SpotPlaceType getType() {
        return type;
    }

    public void setType(SpotPlaceType type) {
        this.type = type;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

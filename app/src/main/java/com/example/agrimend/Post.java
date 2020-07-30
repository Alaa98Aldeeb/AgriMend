package com.example.agrimend;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private String userPhoto;
    private String userName;
    private String userLocation;
    private String plantName;
    private int defaultImg;
    private String timeStamp;

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }


    public Post(String title, String description, String picture, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public String getPlantName() {
        return plantName;
    }

    public Post(String title, String description, String plantName, String picture, String userId, String userName, String userPhoto, String userLocation, String timeStamp) {
        this.title = title;
        this.description = description;
        this.plantName = plantName;
        this.picture = picture;
        this.userId = userId;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userLocation = userLocation;
        this.timeStamp = timeStamp;
    }

    public Post(String title, String description, String plantName, String picture, String userId, String userName, int defaultImg, String userLocation,  String timeStamp) {
        this.title = title;
        this.description = description;
        this.plantName = plantName;
        this.picture = picture;
        this.userId = userId;
        this.userName = userName;
        this.defaultImg = defaultImg;
        this.userLocation = userLocation;
        this.timeStamp = timeStamp;
    }


    // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() { return userName; }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

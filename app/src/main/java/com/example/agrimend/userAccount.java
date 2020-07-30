package com.example.agrimend;

public class userAccount {

    public String userName;
    public String userLocation;
    public String userExperience;
    public String userEmail;
    public String userPassword;


    public userAccount(){

    }

    public userAccount(String userName, String userLocation, String userExperience, String userEmail, String userPassword) {
        this.userName = userName;
        this.userLocation = userLocation;
        this.userExperience = userExperience;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
    public userAccount(String userName, String userLocation, String userEmail, String userPassword) {
        this.userName = userName;
        this.userLocation = userLocation;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLocation() { return userLocation; }

    public void setUserLocation(String userLocation) { this.userLocation = userLocation; }

    public void setUserExperience(String userExperience) { this.userExperience = userExperience; }

    public String getUserExperience() { return userExperience; }
}

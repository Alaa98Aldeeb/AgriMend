package com.example.agrimend;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Comment{

    private String content,uid,uimg,uname, uexpert, commentKey, textSolve;
    private int imgSolveIcon, defaultImg;
    private Boolean click = false;
    private String timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uimg, String uname) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;

    }

    public Comment(String content, String uid, String uimg, String uname, String uexpert) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.uexpert = uexpert;

    }

    public Comment(String content, String uid, String uimg, String uname, String uexpert, String timestamp, String commentKey, int imgSolveIcon, Boolean click) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.uexpert = uexpert;
        this.imgSolveIcon = imgSolveIcon;
        this.timestamp = timestamp;
        this.click = click;
        this.commentKey = commentKey;

    }

    public Comment(String content, String uid, int defaultImg, String uname, String uexpert, String timestamp, String commentKey, int imgSolveIcon, Boolean click) {
        this.content = content;
        this.uid = uid;
        this.defaultImg = defaultImg;
        this.uname = uname;
        this.uexpert = uexpert;
        this.imgSolveIcon = imgSolveIcon;
        this.timestamp = timestamp;
        this.click = click;
        this.commentKey = commentKey;
    }


    public Comment(String content, String uid, String uimg, String uname, Object timestamp, String uexpert) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.uexpert = uexpert;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) { this.uimg = uimg; }

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUexpert() { return uexpert; }

    public void setUexpert(String uexpert) { this.uexpert = uexpert; }

    public String getTimestamp() { return timestamp; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getCommentKey() { return commentKey; }

    public void setCommentKey(String commentKey) { this.commentKey = commentKey; }

    public int getImgSolveIcon() { return imgSolveIcon; }

    public void setImgSolveIcon(int imgSolveIcon) { this.imgSolveIcon = imgSolveIcon; }

    public String getTextSolve() { return textSolve; }

    public void setTextSolve(String textSolve) { this.textSolve = textSolve; }

    public int getDefaultImg() { return defaultImg; }

    public void setDefaultImg(int defaultImg) { this.defaultImg = defaultImg; }

}

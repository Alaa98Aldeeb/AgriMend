package com.example.agrimend;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class newsModel {

    private String title;
    private String description;
    private String pugData;
    private String imageUrl;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPugData(String pugData) {
        this.pugData = pugData;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public newsModel(String title, String description, String pugData, String imageUrl) {
        this.title = title;
        this.description = description;
        this.pugData = pugData;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPugData() {
        return pugData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public newsModel() {
    }
}

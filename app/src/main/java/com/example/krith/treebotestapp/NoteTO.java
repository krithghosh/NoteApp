package com.example.krith.treebotestapp;

import java.io.Serializable;

/**
 * Created by anandmishra on 05/08/16.
 */

public class NoteTO implements Serializable {

    private String title;
    private String description;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

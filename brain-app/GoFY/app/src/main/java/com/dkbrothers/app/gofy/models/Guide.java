package com.dkbrothers.app.gofy.models;

/**
 * Created by kevin on 16/04/2017.
 */

public class Guide {

    private int id;
    private String title;
    private String description;
    private boolean showDescription;

    public Guide() {
    }

    public Guide(int id, String title, String description, boolean showDescription) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.showDescription = showDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isShowDescription() {
        return showDescription;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }
}

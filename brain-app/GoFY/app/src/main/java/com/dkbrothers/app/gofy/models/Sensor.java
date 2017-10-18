package com.dkbrothers.app.gofy.models;

/**
 * Created by kevin on 16/04/2017.
 */

public class Sensor {

    private String id;
    private String title;
    private String description;
    private String type;
    private int idDrawable;


    public Sensor() {
    }

    public Sensor(String id, String title, String description, String type, int idDrawable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.idDrawable = idDrawable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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


}

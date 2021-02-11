/*Classe Java per istanziare ogni oggetto birra ricevuto
* in risposta dal server*/

package com.maxp.punkbeers;

public class Beer {

    private int id;
    private String name;
    private String tagline;
    private String description;
    private String image_url;


    public int getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

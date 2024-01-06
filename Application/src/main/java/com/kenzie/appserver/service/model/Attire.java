package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Attire {
    private String id;
    private String name;
    private String type;
    private String imageURL;

    public Attire(String id, String name, String type, String imageURL) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attire attire = (Attire) o;
        return Objects.equals(id, attire.id) && Objects.equals(name, attire.name) && Objects.equals(type, attire.type) && Objects.equals(imageURL, attire.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, imageURL);
    }

    @Override
    public String toString() {
        return "Attire{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}

package io.antelli.plugin.csfd.entity;

public class Artist {

    private String name;
    private String link;

    public Artist(String name, String link) {
        this.setName(name);
        this.setLink(link);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

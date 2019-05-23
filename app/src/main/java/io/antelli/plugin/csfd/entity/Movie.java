package io.antelli.plugin.csfd.entity;

import java.util.List;

public class Movie {
    private String name;
    private String nameEn;
    private String genre;
    private String rating;
    private String poster;
    private String description;
    private List<ArtistGroup> artists;
    private String link;
    private String origin;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public List<ArtistGroup> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistGroup> artists) {
        this.artists = artists;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getMusicArtist(){
        for (ArtistGroup artist : artists) {
            if (artist.getName().equals("Hudba:")){
                return artist.getArtists().get(0).getName();
            }
        }
        return null;
    }
}

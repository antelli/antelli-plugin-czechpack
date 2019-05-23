package io.antelli.plugin.csfd.entity;

import java.util.List;

public class ArtistGroup {
    private String name;
    private List<Artist> artists;

    public ArtistGroup(String name, List<Artist> artists) {
        this.name = name;
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}

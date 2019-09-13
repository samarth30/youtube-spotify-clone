package com.example.dell.spotify_clone_main.adapters;

public class Playlist {
    String id,name;

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist(String name) {
        this.name = name;
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
}

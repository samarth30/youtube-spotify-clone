package com.example.dell.spotify_clone_main.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
// youtube thummbnail class
public class Thumbnails {


    @SerializedName("medium")
    @Expose
    private Medium medium;


    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }


}
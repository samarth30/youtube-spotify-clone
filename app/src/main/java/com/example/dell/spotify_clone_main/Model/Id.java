package com.example.dell.spotify_clone_main.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
// youtube id
public class Id {


    @SerializedName("videoId")
    @Expose
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
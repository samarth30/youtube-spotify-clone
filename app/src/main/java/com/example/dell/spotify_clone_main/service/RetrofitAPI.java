package com.example.dell.spotify_clone_main.service;
// youtube retrofit api
public class RetrofitAPI {
    public static final String Url = "https://www.googleapis.com/youtube/v3/";

    public static DataAPI getdata() {
        return Retrofitlnit.getclient(Url).create(DataAPI.class);
    }
}

package com.example.dell.spotify_clone_main.spotify_files;

public class ExampleItem {
    private String mImageUrl;
    private String mTitle,mShortDescription;
    private int mRating,mPrice;
    String uri;
    public ExampleItem(String imageUrl, String title, int rating, int price, String shortDescription){
        mImageUrl = imageUrl;
        mTitle = title;
        mRating = rating;
        mPrice = price;
        mShortDescription = shortDescription;
    }

    public ExampleItem(String mTitle, String uri) {
        this.mTitle = mTitle;
        this.uri = uri;
    }

    public ExampleItem(String mImageUrl, String mTitle, String uri) {
        this.mImageUrl = mImageUrl;
        this.mTitle = mTitle;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public ExampleItem(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getImageUrl(){
        return  mImageUrl;

    }
    public String getmShortDescription(){
        return  mShortDescription;

    }
    public int getPrice(){
        return  mPrice;

    }

    public String getmTitle(){
        return mTitle;
    }
    public int getRating(){
        return mRating;
    }

}

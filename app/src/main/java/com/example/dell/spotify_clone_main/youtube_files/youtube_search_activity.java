package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.Model.Medium;
import com.example.dell.spotify_clone_main.Model.ModelData;
import com.example.dell.spotify_clone_main.Model.Thumbnails;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.service.DataAPI;
import com.example.dell.spotify_clone_main.service.RetrofitAPI;
import com.example.dell.spotify_clone_main.Model.Item;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class youtube_search_activity extends AppCompatActivity {
    public static String API_KEY = "AIzaSyCGRsTeKpXw89plhwXNFvkUCzrNzm8jw78";
    EditText edtsearch;
    Button btnsearch;
    ListView listView;
    YoutubeAdapter youtubeAdapter;
    ArrayList<Item> mangitem;
    public static String idvideo;
    String title;
    String thumbnail;
    TextView noresults;
    ProgressBar progressBar;

    TextView searchsomething;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        noresults = findViewById(R.id.noResultsy);
        progressBar = findViewById(R.id.progressbaryoutube);
        Anhxa();
        searchsomething = findViewById(R.id.searchidy);
        String text = getIntent().getExtras().getString("search");
        if(!text.equals("")) {

            edtsearch.setText(text);
            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            String tukhoa = text;
            tukhoa =tukhoa.replace(" ","%20");
            Docdulieu(tukhoa);
        }

        if(edtsearch.getText().toString().isEmpty()){
            searchsomething.setVisibility(View.VISIBLE);
        }

        edtsearch.setOnEditorActionListener(editorActionListener);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                String tukhoa = edtsearch.getText().toString();
                tukhoa =tukhoa.replace(" ","%20");
                Docdulieu(tukhoa);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(youtube_search_activity.this,playvideoActivity.class);
                 idvideo = mangitem.get(position).getId().getVideoId();
                 Item currentItem = mangitem.get(position);
                 title = currentItem.getSnippet().getTitle();
                 thumbnail = currentItem.getSnippet().getThumbnails().getMedium().getUrl();
                 intent.putExtra("title",title);
                 intent.putExtra("thumbnail",thumbnail);
                 intent.putExtra("videoId",idvideo);
                 startActivity(intent);
            }
        });


    }

    // parse data from youtube api to list view
    public void Docdulieu(String tukhoa) {
        progressBar.setVisibility(View.VISIBLE);
        DataAPI dataAPI = RetrofitAPI.getdata();
        Call<ModelData> callback = dataAPI.getResurt("snippet", tukhoa, "50", "video", "AIzaSyCGRsTeKpXw89plhwXNFvkUCzrNzm8jw78");
        callback.enqueue(new Callback<ModelData>() {
            @Override
            public void onResponse(Call<ModelData> call, Response<ModelData> response) {
                ModelData modelData = response.body();
                mangitem = (ArrayList<Item>) modelData.getItems();
                if(mangitem.size() == 0){
                    progressBar.setVisibility(View.GONE);
                    noresults.setVisibility(View.VISIBLE);
                }
                else {
                    youtubeAdapter = new YoutubeAdapter(youtube_search_activity.this, android.R.layout.simple_list_item_1, mangitem);
                    progressBar.setVisibility(View.GONE);
                        listView.setAdapter(youtubeAdapter);
                        listView.setVisibility(View.VISIBLE);


                }
            }
            @Override
            public void onFailure(Call<ModelData> call, Throwable t) {
                noresults.setVisibility(View.VISIBLE);
            }
        });

    }

    private void Anhxa() {
        edtsearch = findViewById(R.id.edittextSearch);
        btnsearch = findViewById(R.id.buttonSearch);
        listView = findViewById(R.id.listview);
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEARCH:

                    if(edtsearch.getText().toString().isEmpty()){
                        listView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        searchsomething.setVisibility(View.VISIBLE);
                    }else {
                        searchsomething.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        String tukhoa = edtsearch.getText().toString();
                        tukhoa = tukhoa.replace(" ", "%20");
                        Docdulieu(tukhoa);
                    }
                    break;
            }
            return false;
        }

    };
}

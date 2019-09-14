package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
import com.example.dell.spotify_clone_main.adapters.Playlist;
import com.example.dell.spotify_clone_main.adapters.PlaylistRecyclerView;
import com.example.dell.spotify_clone_main.adapters.RecyclerItemClickListener;
import com.example.dell.spotify_clone_main.spotify_files.ExampleItem;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class detailActivity extends AppCompatActivity {

    RecyclerView PlayListRecyclerView;
    ExampleAdapter adapter;
    ArrayList<ExampleItem> playlistList;
    String playlist_detail = "https://aasthamalik31.pythonanywhere.com/playlist/playlist_detail/";
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        id = getIntent().getExtras().getInt("id");

        playlistList = new ArrayList<>();

        parseData();
        PlayListRecyclerView = findViewById(R.id.recyclerViewPlaylist);
        PlayListRecyclerView.setHasFixedSize(true);
        PlayListRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        adapter = new ExampleAdapter(this,playlistList);
        PlayListRecyclerView.setAdapter(adapter);

        PlayListRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ExampleItem currentItem = playlistList.get(position);
                        String type = currentItem.getType();
                        String youtube = "youtube";
                        String spotify = "spotify";
                        if(type.equals(youtube)){
                            Intent intent = new Intent(detailActivity.this,playvideoActivity.class);
                            String videoid = currentItem.getId();
                            String name = currentItem.getmTitle();
                            String image = currentItem.getImageUrl();
                            intent.putExtra("videoId",videoid);
                            intent.putExtra("title",name);
                            intent.putExtra("thumbnail",image);
                            startActivity(intent);
                        }
                        else if(type.equals("spotify")){
                            Intent intent = new Intent(detailActivity.this,com.example.dell.spotify_clone_main.spotify_files.rsplayer.class);
                            String songid = currentItem.getId();
                            String name = currentItem.getmTitle();
                            String image = currentItem.getImageUrl();
                            intent.putExtra("title",name);
                            intent.putExtra("image",image);
                            intent.putExtra("uri",songid);
                            startActivity(intent);
                        }

                    }
                })
        );

    }

    private void parseData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, playlist_detail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String type = jsonObject.getString("type");
                        String track_id = jsonObject.getString("track_id");
                        String image = jsonObject.getString("image");
                        String name = jsonObject.getString("name");

                        playlistList.add(new ExampleItem(image,name,track_id,type));
                        adapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(detailActivity.this);
                String token = sharedPrefManager.loadToken();
                params.put("token",token);
                params.put("playlist", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;
import com.example.dell.spotify_clone_main.adapters.DetailActivityAdapter;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
import com.example.dell.spotify_clone_main.adapters.Playlist;
import com.example.dell.spotify_clone_main.adapters.PlaylistRecyclerView;
import com.example.dell.spotify_clone_main.adapters.RecyclerItemClickListener;
import com.example.dell.spotify_clone_main.adapters.ExampleItem;
import com.example.dell.spotify_clone_main.adapters.viewCollab;
import com.example.dell.spotify_clone_main.adapters.viewCollabAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// detail page activity opened in playlist viewer
public class detailActivity extends AppCompatActivity {

    RecyclerView PlayListRecyclerView;
    ExampleAdapter adapter;
    DetailActivityAdapter detailActivityAdapter;
    ArrayList<ExampleItem> playlistList;
    String playlist_detail = "https://aasthamalik31.pythonanywhere.com/playlist/playlist_detail/";
    String view_collab = "https://aasthamalik31.pythonanywhere.com/playlist/view_collab/";
    int id;

    ArrayList<viewCollab> viewcollabList;
    viewCollabAdapter ViewCollabAdapter;
    RecyclerView viewCollabRecyclerView;
    TextView textView;
    ProgressBar progressBar;
    ProgressBar progressBarViewCollab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        id = getIntent().getExtras().getInt("id");
        progressBar = findViewById(R.id.progressbardetail);
        progressBar.setVisibility(View.VISIBLE);
        playlistList = new ArrayList<>();
        textView = findViewById(R.id.textviewdetailpage);
        parseData();
        PlayListRecyclerView = findViewById(R.id.recyclerViewPlaylist);
        PlayListRecyclerView.setHasFixedSize(true);
        PlayListRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        detailActivityAdapter = new DetailActivityAdapter(this,playlistList);
        PlayListRecyclerView.setAdapter(detailActivityAdapter);

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

        FloatingActionButton viewcollab = findViewById(R.id.viewCollab);
        viewcollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCollabView();
            }
        });

    }
    // dialog to see collaborations
    private void openDialogCollabView() {
        final AlertDialog.Builder mb = new AlertDialog.Builder(this);
        final View dialog = LayoutInflater.from(this).inflate(R.layout.view_collab, null, false);


        viewcollabList = new ArrayList<>();
        viewCollabRecyclerView = dialog.findViewById(R.id.view_collaborations);
        progressBarViewCollab = dialog.findViewById(R.id.progressbarViewCollab);
        progressBarViewCollab.setVisibility(View.VISIBLE);
        viewCollabRecyclerView.setHasFixedSize(true);
        viewCollabRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        parseCollaborations();
        ViewCollabAdapter = new viewCollabAdapter(this,viewcollabList);
        viewCollabRecyclerView.setAdapter(ViewCollabAdapter);

        mb.setView(dialog);
        final AlertDialog ass = mb.create();

        ass.show();

    }

    // add collaborations to recycler view
    private void parseCollaborations() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, view_collab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    viewcollabList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String email = jsonObject.getString("email");

                        viewcollabList.add(new viewCollab(email));
                        ViewCollabAdapter.notifyDataSetChanged();
                    }
                 progressBarViewCollab.setVisibility(View.GONE);

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
                params.put("playlist_id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // add videos and songs to your playlist recycler view
    private void parseData() {
        progressBar.setVisibility(View.VISIBLE);
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
                        detailActivityAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                   if(playlistList.size() == 0){
                       textView.setVisibility(View.VISIBLE);
                       PlayListRecyclerView.setVisibility(View.GONE);
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

package com.example.dell.spotify_clone_main.spotify_files;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;
import com.example.dell.spotify_clone_main.adapters.Playlist;
import com.example.dell.spotify_clone_main.adapters.PlaylistRecyclerView;
import com.example.dell.spotify_clone_main.adapters.RecyclerItemClickListener;
import com.example.dell.spotify_clone_main.youtube_files.Search;
import com.example.dell.spotify_clone_main.youtube_files.detailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//  playlist in spotify fragment playlist
public class spotify_playlist extends Fragment {

    RequestQueue requestQueue;
    String url = "https://aasthamalik31.pythonanywhere.com/playlist/create_new_playlist/";
    String url1= "https://aasthamalik31.pythonanywhere.com/playlist/my_playlists/";
    View view;
    Context context;
    RecyclerView spotifyPlayListRecyclerView;
    PlaylistRecyclerView adapter;
    ArrayList<Playlist> playlistList;

    ProgressBar progressBar;
    public spotify_playlist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youtube_playlist, container, false);
        context = view.getContext();
        progressBar = view.findViewById(R.id.progressbar);
        requestQueue = Volley.newRequestQueue(context);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);

        playlistList = new ArrayList<>();

        parseData();
        spotifyPlayListRecyclerView = view.findViewById(R.id.recyclerViewPlaylist);
        spotifyPlayListRecyclerView.setHasFixedSize(true);
        spotifyPlayListRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        adapter = new PlaylistRecyclerView(context,playlistList);
        spotifyPlayListRecyclerView.setAdapter(adapter);

        spotifyPlayListRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Playlist currentItem = playlistList.get(position);
                        Intent intent = new Intent(context, detailActivity.class);
                        int id = currentItem.getId();
                        intent.putExtra("id",id);
                        startActivity(intent);

                    }
                })
        );
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        FloatingActionButton search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Search.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // parse data to recycler view
    private void parseData() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    playlistList.clear();
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        playlistList.add(new Playlist(name,id));
                        adapter.notifyDataSetChanged();
                    }

                    progressBar.setVisibility(View.GONE);
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
                SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
                String token = sharedPrefManager.loadToken();
                params.put("token",token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    // open dialog to create playlist
    private void openDialog() {
//        Dialog dialog = new Dialog();
//        dialog.show(getFragmentManager(),"dialog");
        final AlertDialog.Builder mb = new AlertDialog.Builder(getActivity());
        final View dialog = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog, null, false);

        final EditText playlistName = dialog.findViewById(R.id.dialog_playlistname);
        final EditText PlaylistPassword = dialog.findViewById(R.id.dialog_password);



        mb.setView(dialog)
                .setTitle("login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playlistname = playlistName.getText().toString();
                        String password = PlaylistPassword.getText().toString();
                        addTexts(playlistname,password);
                    }
                });


//        final Button ok = dialog.findViewById(R.id.ok);
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String playlistname = playlistName.getText().toString();
//                String password = PlaylistPassword.getText().toString();
//                addTexts(playlistname,password);
//            }
//        });

        mb.setView(dialog);
        final AlertDialog ass = mb.create();

        ass.show();
    }

    // add playlist to server
    public void addTexts(final String playlistname, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Boolean PlaylistCreated = jsonObject.getBoolean("Playlist Created");
                    if(PlaylistCreated){
                        Toast.makeText(context, "playlist created", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "this playlist already exists", Toast.LENGTH_SHORT).show();
            }
        } ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
                String token = sharedPrefManager.loadToken();
                params.put("token",token);
                params.put("name", playlistname);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
        parseData();
    }
}

package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
import com.example.dell.spotify_clone_main.adapters.RecyclerItemClickListener;
import com.example.dell.spotify_clone_main.spotify_files.ExampleItem;
import com.example.dell.spotify_clone_main.spotify_files.SearchActivity;
import com.example.dell.spotify_clone_main.spotify_files.rsplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {
    String key;
    String url = "https://aasthamalik31.pythonanywhere.com/playlist/search_playlist/";
    Button searchButton;
    ArrayList<ExampleItem> exampleItemList;
    ExampleAdapter exampleAdapter;
    EditText searchText;
    RecyclerView recyclerViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        searchText = findViewById(R.id.editText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = searchText.getText().toString();
                parseData();
            }
        });
        exampleItemList = new ArrayList<>();


        recyclerViewSearch = findViewById(R.id.recyclerView);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        exampleAdapter = new ExampleAdapter(this,exampleItemList);
        recyclerViewSearch.setAdapter(exampleAdapter);

        recyclerViewSearch.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        ExampleItem currentItem = exampleItemList.get(position);
//
//                        Intent a=new Intent(Search.this, rsplayer.class);
//                        a.putExtra("uri",currentItem.getUri());
//                        a.putExtra("image",currentItem.getImageUrl());
//                        a.putExtra("title",currentItem.getmTitle());
//                        startActivity(a);
                    }
                })
        );
    }

    private void parseData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    exampleItemList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");

                        exampleItemList.add(new ExampleItem(name));
                        exampleAdapter.notifyDataSetChanged();
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
                Map<String,String> params = new HashMap<>();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(Search.this);
                String token = sharedPrefManager.loadToken();

                params.put("token",token);
                params.put("search_term",key);
                params.put("password","");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

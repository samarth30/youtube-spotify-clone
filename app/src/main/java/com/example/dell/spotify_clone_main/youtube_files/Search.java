package com.example.dell.spotify_clone_main.youtube_files;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.dell.spotify_clone_main.adapters.ExampleItem;
import com.example.dell.spotify_clone_main.adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// search playlist
public class Search extends AppCompatActivity {
    String key="s";
    String url = "https://aasthamalik31.pythonanywhere.com/playlist/search_playlist/";
    String addCollab = "https://aasthamalik31.pythonanywhere.com/playlist/add_collab/";
    Button searchButton;
    ArrayList<ExampleItem> exampleItemList;
    SearchAdapter exampleAdapter;
    EditText searchText;
    RecyclerView recyclerViewSearch;
   ProgressBar progressBar;
   TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        progressBar = findViewById(R.id.progressbarSearch);
        searchText = findViewById(R.id.editTextsearchplaylist);
        searchText.setOnEditorActionListener(editorActionListener);
        textView = findViewById(R.id.textsearchplaylista);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewSearch.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                key = searchText.getText().toString();
                parseData(key);
            }
        });
        exampleItemList = new ArrayList<>();

        recyclerViewSearch = findViewById(R.id.recyclerView);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        exampleAdapter = new SearchAdapter(this,exampleItemList);
        recyclerViewSearch.setAdapter(exampleAdapter);

        recyclerViewSearch.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ExampleItem currentItem = exampleItemList.get(position);
                        String id = currentItem.getPlaylistID();
                        openDialog(id);
                    }
                })
        );
    }

//  add collaboration dialog
    private void openDialog(final String id) {
        final AlertDialog.Builder mb = new AlertDialog.Builder(this);
        final View dialog = LayoutInflater.from(this).inflate(R.layout.dialogcollab, null, false);

        final EditText PlaylistPassword = dialog.findViewById(R.id.password);



        mb.setView(dialog)
                .setTitle("Add Collaboration")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = PlaylistPassword.getText().toString();
                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(Search.this, "please enter something in password", Toast.LENGTH_SHORT).show();
                        } else {
                            AddCollab(id, password);
                        }
                    }
                });


        mb.setView(dialog);
        final AlertDialog ass = mb.create();

        ass.show();
    }
//  add collab to server
    private void AddCollab(final String id,final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, addCollab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                  JSONObject jsonObject = new JSONObject(response);
                  Boolean collab = jsonObject.getBoolean("Collab Created");

                  if(collab){
                      Toast.makeText(Search.this, "Collaboration done succesfully now this is your playlist", Toast.LENGTH_SHORT).show();
                  }else{
                      Toast.makeText(Search.this, "please enter the correct password", Toast.LENGTH_SHORT).show();
                  }

                } catch (JSONException e) {
                    Toast.makeText(Search.this, "you cannot add collab to your playlist" , Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Search.this, "something went wrong either you have entered collab to your playlist or there some internet connection error", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(Search.this);
                String token = sharedPrefManager.loadToken();

                params.put("token",token);
                params.put("playlist_id",id);
                params.put("password",password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
// parse data to recycler view
    private void parseData(final String key) {
     progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    exampleItemList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String id = jsonObject.getString("id");

                        exampleItemList.add(new ExampleItem(name,id));
                        exampleAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                   if(exampleItemList.size() == 0){
                       textView.setVisibility(View.VISIBLE);
                   }else{
                       recyclerViewSearch.setVisibility(View.VISIBLE);

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

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEND:
                    recyclerViewSearch.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    key = searchText.getText().toString();
                    parseData(key);
            }
            return false;
        }

    };
}


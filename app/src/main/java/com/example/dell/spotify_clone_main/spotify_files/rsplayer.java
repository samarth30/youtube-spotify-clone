// spotify player
package com.example.dell.spotify_clone_main.spotify_files;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;
import com.example.dell.spotify_clone_main.adapters.AddToPlaylistAdapter;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
import com.example.dell.spotify_clone_main.adapters.ExampleItem;
import com.example.dell.spotify_clone_main.adapters.Playlist;
import com.example.dell.spotify_clone_main.adapters.PlaylistRecyclerView;
import com.example.dell.spotify_clone_main.adapters.RecyclerItemClickListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Player.NotificationCallback;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class rsplayer extends AppCompatActivity implements
        NotificationCallback, ConnectionStateCallback
{


    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "d97e6af9d329405d997632c60fe79a16";

    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "http://vibin.in/callback/";
    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;

    ImageButton play;
    String key = "Muse";
    String url1 = "https://api.spotify.com/v1/search?q=";
    String url2 = "&type=track%2Cartist&market=US&limit=10&offset=5";

    RequestQueue requestQueue;
    Button searchButton;
    ArrayList<ExampleItem> exampleItemList;
    ExampleAdapter exampleAdapter;
    EditText searchText;
    RecyclerView recyclerViewSearch;
    long lengthms;
    String AcessToken;
    Handler handler;
    Runnable my;
    SeekBar seekbar;
    boolean seekusedbyuser=false;
    boolean istouching=false;
    boolean killMe = false;
    String uri;
    String ImageURL,Title;

    Button addToPlayList;

    AddToPlaylistAdapter addToPlaylistAdapter;

    RecyclerView youtubePlayListRecyclerView;
    PlaylistRecyclerView adapter;
    ArrayList<Playlist> playlistList;

    String myPlaylist= "https://aasthamalik31.pythonanywhere.com/playlist/my_playlists/";
    String addtrak = "https://aasthamalik31.pythonanywhere.com/playlist/add_trak_to_playlist/";


    TextView textView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsplayer);
        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        progressBar = findViewById(R.id.progressbarPlayList);
        final AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        ImageURL = getIntent().getExtras().getString("image");
        Title = getIntent().getExtras().getString("title");
         ImageView image = findViewById(R.id.imageRsplayer);
        TextView heading = findViewById(R.id.textviewtitlespotify);
        heading.setText(Title);
        Glide.with(this).load(ImageURL).into(image);


        AcessToken = getIntent().getExtras().getString("token");

        boolean playing=true;
        final Button Play_Pause =(Button) findViewById(R.id.button3);
        Play_Pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mPlayer.getPlaybackState().isPlaying)
                {
                    mPlayer.pause(null);
                    Play_Pause.setBackground(getDrawable(R.drawable.ic_play_circle_filled_black_24dp));
                }
                else {
                    mPlayer.resume(null);
                    Play_Pause.setBackground(getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }


            }
        });

        seekbar =(SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                seekusedbyuser=true;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress=seekBar.getProgress();
                seekusedbyuser=false;

                seekusedbyuser=true;

                int to = (int) (lengthms * progress / 100);

                mPlayer.seekToPosition(null, to);
                mPlayer.resume(null);

            }
        });
        seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        addToPlayList = findViewById(R.id.addToPlayList);
        addToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause(null);
                dialog();
            }
        });
    }
    // dialog add to playlist
    private void dialog() {
        final AlertDialog.Builder mb = new AlertDialog.Builder(this);
        final View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_playlist, null, false);

        playlistList = new ArrayList<>();
        progressBar = dialog.findViewById(R.id.progressbarPlayList);
        youtubePlayListRecyclerView = dialog.findViewById(R.id.playlists);
        textView = dialog.findViewById(R.id.textviewplaylistplayer);
        youtubePlayListRecyclerView.setHasFixedSize(true);
        youtubePlayListRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        parseData();
        addToPlaylistAdapter= new AddToPlaylistAdapter(this,playlistList);
        youtubePlayListRecyclerView.setAdapter(addToPlaylistAdapter);


        youtubePlayListRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Playlist currentItem = playlistList.get(position);
                        int id = currentItem.getId();
                        AddThisToPlaylist(id,Title,ImageURL);
                    }
                })
        );

        mb.setView(dialog);
        final AlertDialog ass = mb.create();

        ass.show();
    }

    // add song to server
    private void AddThisToPlaylist( final int id,final String title,final String thumbnail) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addtrak, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean trackadded = jsonObject.getBoolean("Track added");

                    if(trackadded){
                        Toast.makeText(rsplayer.this, "track succesfully added", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(rsplayer.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(rsplayer.this);
                String token = sharedPrefManager.loadToken();
                params.put("token",token);
                params.put("playlist", String.valueOf(id));
                params.put("type","spotify");
                params.put("track_id",uri);
                params.put("name",title);
                params.put("image",thumbnail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // parse the playlists to recycler view
    private void parseData() {
progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myPlaylist, new Response.Listener<String>() {
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
                        addToPlaylistAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                    if(playlistList.size() == 0){
                        textView.setVisibility(View.VISIBLE);
                    }else{
                        textView.setVisibility(View.GONE);
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
                SharedPrefManager sharedPrefManager = new SharedPrefManager(rsplayer.this);
                String token = sharedPrefManager.loadToken();
                params.put("token",token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
// spotify fucntions

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {

                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(rsplayer.this);
                        mPlayer.addNotificationCallback(rsplayer.this);
                        mPlayer.playUri(null,uri , 0, 0);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {


    }

    @Override
    public void onLoggedOut() {


    }

    @Override
    public void onLoginFailed(Error error) {


    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }


    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {



        if (mPlayer.getPlaybackState().isPlaying)
            lengthms=mPlayer.getMetadata().currentTrack.durationMs;

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    // i have been touched
                    istouching = false;

                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    // you can implement this

                    istouching = true;
                    return true;
                }
                return false;
            }
        });


        handler = new Handler();
        my=new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds

                if(killMe)
                    return;

                handler.postDelayed(this, 1000);

                float wow= mPlayer.getPlaybackState().positionMs;
                float wowInt= ((wow/lengthms)*100);
                if (seekusedbyuser==false||istouching==false)
                { seekbar.setProgress((int) wowInt);}

            }
        };
        handler.postDelayed(my, 2000);



    }

    @Override
    public void onPlaybackError(Error error) {


    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null)
            handler.removeCallbacksAndMessages(my);
        killMe=true;

        mPlayer.pause(null);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handler!=null)
            handler.removeCallbacksAndMessages(my);
        killMe=true;

        mPlayer.pause(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (handler!=null)
            handler.removeCallbacksAndMessages(my);
        killMe=true;

        mPlayer.pause(null);

    }
}
package com.example.dell.spotify_clone_main.spotify_files;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
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

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsplayer);



        final AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        String ImageURL = getIntent().getExtras().getString("image");
        ImageView image = findViewById(R.id.imageRsplayer);

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

    }


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
                        Intent intent = getIntent();
                        String id = intent.getStringExtra("uri");
                        mPlayer.playUri(null,id , 0, 0);




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
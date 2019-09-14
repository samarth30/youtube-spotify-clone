package com.example.dell.spotify_clone_main.spotify_files;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.adapters.ExampleAdapter;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;

public class spotify_player_activity_main2 extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private static final String CLIENT_ID = "d97e6af9d329405d997632c60fe79a16";


    private static final String REDIRECT_URI = "http://vibin.in/callback/";
    private static final int REQUEST_CODE = 1337;
    private Player mPlayer;

    ImageButton play;
    String key = "Muse";
    String url1 = "https://api.spotify.com/v1/search?q=";
    String url2 = "&type=track%2Cartist&market=US&limit=10&offset=5";

    String url = "https://api.spotify.com/v1/search?q="+key+"&type=track&market=US&limit=10&offset=5";
    RequestQueue requestQueue;
    Button searchButton;
    ArrayList<ExampleItem> exampleItemList;
    ExampleAdapter exampleAdapter;
    EditText searchText;
    RecyclerView recyclerViewSearch;

    String uri,title,image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_player_main);

        uri = getIntent().getExtras().getString("uri");
        title = getIntent().getExtras().getString("name");
        image = getIntent().getExtras().getString("image");
        final AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        // The next 19 lines of the code are what you need to copy & paste! :)
        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(spotify_player_activity_main2.this);
                        mPlayer.addNotificationCallback(spotify_player_activity_main2.this);
//                        Intent intent1 = new Intent(spotify_player_activity_main2.this,rsplayer.class);
//                        intent1.putExtra("token",response.getAccessToken());
//                        intent1.putExtra("uri",uri);
//                        intent1.putExtra("title",title);
//                        intent1.putExtra("image",image);
//
//                        startActivity(intent1);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("this", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }


    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("this", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("this", "Playback error received: " + error.name());
        switch (error) {
            // Handle error ty0pe as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("this", "User logged in");

        // This is the line that plays a song.
//        mPlayer.playUri(null, "spotify:track:7hH4dSp71EOv3XS57e8CYu", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("this", "User logged out");
    }

    @Override
    public void onLoginFailed(Error var1) {
        Log.d("this", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("this", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("this", "Received connection message: " + message);
    }
}
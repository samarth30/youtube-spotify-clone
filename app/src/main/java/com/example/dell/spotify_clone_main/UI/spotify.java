package com.example.dell.spotify_clone_main.UI;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.spotify_files.spotify_playlist;
import com.example.dell.spotify_clone_main.spotify_files.spotify_search;
import com.example.dell.spotify_clone_main.spotify_files.spotify_user;

public class spotify extends AppCompatActivity {

    FrameLayout mMainFrame;

    spotify_playlist spotify_playlist;
    spotify_search spotify_search;
    spotify_user spotify_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        final BottomNavigationView mMainNav = findViewById(R.id.nav_view);

        mMainFrame = (FrameLayout)findViewById(R.id.spotify_frame);
        spotify_playlist = new spotify_playlist();
        spotify_user = new spotify_user();
        spotify_search = new spotify_search();

        setFragment(spotify_playlist);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        setFragment(spotify_search);
                        return true;

                    case R.id.navigation_playlist:
                        setFragment(spotify_playlist);
                        return true;

                    case R.id.navigation_user:
                        setFragment(spotify_user);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.spotify_frame,fragment);
        fragmentTransaction.commit();
    }
}

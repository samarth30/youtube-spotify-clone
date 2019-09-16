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
import com.example.dell.spotify_clone_main.youtube_files.youtube_playlist;
import com.example.dell.spotify_clone_main.youtube_files.youtube_search;
import com.example.dell.spotify_clone_main.youtube_files.youtube_user;
// youtube main
public class youtube extends AppCompatActivity {
    FrameLayout mMainFrame;

    youtube_playlist youtube_Playlist;
    youtube_user youtube_User;
    youtube_search youtube_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        final BottomNavigationView mMainNav = findViewById(R.id.nav_view);

        mMainFrame = (FrameLayout)findViewById(R.id.youtube_frame);
        youtube_Playlist = new youtube_playlist();
        youtube_Search = new youtube_search();
        youtube_User = new youtube_user();


        setFragment(youtube_Search);
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        setFragment(youtube_Search);
                        return true;

                    case R.id.navigation_playlist:
                        setFragment(youtube_Playlist);
                        return true;

                    case R.id.navigation_user:
                        setFragment(youtube_User);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.youtube_frame,fragment);
        fragmentTransaction.commit();
    }

}

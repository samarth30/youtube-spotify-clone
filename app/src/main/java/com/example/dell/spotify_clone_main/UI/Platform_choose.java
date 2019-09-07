package com.example.dell.spotify_clone_main.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.spotify_clone_main.R;

public class Platform_choose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_choose);

        final Button youtube = findViewById(R.id.youtube);
        final Button spotify = findViewById(R.id.spotify);

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Platform_choose.this, com.example.dell.spotify_clone_main.UI.youtube.class));
            }
        });

        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Platform_choose.this, com.example.dell.spotify_clone_main.UI.spotify.class));
            }
        });
    }
}

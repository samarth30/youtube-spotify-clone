package com.example.dell.spotify_clone_main.spotify_files;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.youtube_files.youtube_search_activity;

public class spotify_search extends Fragment {


    View view;
    Context mContext;

    public spotify_search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_spotify_search, container, false);
        mContext = view.getContext();

        Button button = view.findViewById(R.id.buttonSearch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, spotify_player_activity_main.class));
            }
        });

        return view;
    }
}

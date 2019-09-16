package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;

// yotube user

public class youtube_user extends Fragment {
    View view;
    Context context;
    public youtube_user() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_youtube_user, container, false);
        context = view.getContext();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String email = sharedPrefManager.loadEmail();
        TextView textView = view.findViewById(R.id.textviewuser);
        textView.setText(email);
        return  view;
    }
}

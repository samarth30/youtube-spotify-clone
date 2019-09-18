package com.example.dell.spotify_clone_main.spotify_files;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.UI.LoginAct;
import com.example.dell.spotify_clone_main.UI.SharedPrefManager;

//spotify user fragment
public class spotify_user extends Fragment {

    View view;
    Context mContext;

    public spotify_user() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_youtube_user, container, false);
        mContext = view.getContext();

        //
        SharedPrefManager sharedPrefManager = new SharedPrefManager(mContext);
        String email = sharedPrefManager.loadEmail();
        TextView textView = view.findViewById(R.id.textviewuser);
        textView.setText(email);

        Button Log_out = (Button) view.findViewById(R.id.log_out);
        Log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(mContext).logout();

                getActivity().finishAffinity();

            }
        });


        return view;
    }
}

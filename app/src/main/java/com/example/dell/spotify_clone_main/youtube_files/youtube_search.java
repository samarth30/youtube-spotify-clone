package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.R;
import com.example.dell.spotify_clone_main.spotify_files.spotify_player_activity_main;

// youtube search fragment
public class youtube_search extends Fragment {

    View view;
    Context mContext;
    EditText editTextsearch;
    Button searchEditText;
    public youtube_search() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_youtube_search, container, false);
        mContext = view.getContext();

        editTextsearch = view.findViewById(R.id.edittextSearch);
        searchEditText = view.findViewById(R.id.button);
        editTextsearch.setOnEditorActionListener(editorActionListener);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String text = editTextsearch.getText().toString();
                 Intent intent = new Intent(mContext,youtube_search_activity.class);
                 intent.putExtra("search",text);
                 startActivity(intent);
            }
        });

        Button button = view.findViewById(R.id.buttonSearch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,youtube_search_activity.class));
            }
        });
        return view;
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId){
                case EditorInfo.IME_ACTION_SEND:
                    String text = editTextsearch.getText().toString();
                    Intent intent = new Intent(mContext,youtube_search_activity.class);
                    intent.putExtra("search",text);
                    startActivity(intent);
            }
            return false;
        }

    };

}

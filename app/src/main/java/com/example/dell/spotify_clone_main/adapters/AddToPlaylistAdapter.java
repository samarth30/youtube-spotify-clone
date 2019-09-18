package com.example.dell.spotify_clone_main.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.R;

import java.util.ArrayList;

// playlist recycler view adapter
public class AddToPlaylistAdapter extends RecyclerView.Adapter<AddToPlaylistAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Playlist> mExampleList;

    public AddToPlaylistAdapter(Context context, ArrayList<Playlist> exampleList){
        mContext = context;
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_playlist_dialod_vertical,parent,false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        Playlist currentItem = mExampleList.get(position);
//        String imageUrl = currentItem.getImageUrl();
        String name = currentItem.getName();


        holder.mTextViewTitle.setText(name);
//        Glide.with(mContext).load(imageUrl).into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewTitle;
        public View view;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewaddtoplaylist);
            mTextViewTitle = itemView.findViewById(R.id.textViewtitle);
            view = itemView.findViewById(R.id.view);

        }
    }
}

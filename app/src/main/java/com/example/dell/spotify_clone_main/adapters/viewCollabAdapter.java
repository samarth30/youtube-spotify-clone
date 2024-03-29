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
// view collab adapter
public class viewCollabAdapter extends RecyclerView.Adapter<viewCollabAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<viewCollab> mExampleList;

    public viewCollabAdapter(Context context, ArrayList<viewCollab> exampleList){
        mContext = context;
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_collab_xml,parent,false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        viewCollab currentItem = mExampleList.get(position);
//        String imageUrl = currentItem.getImageUrl();
        String email = currentItem.getEmail();


        holder.mTextViewTitle.setText(email);
//        Glide.with(mContext).load(imageUrl).into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    public class ExampleViewHolder extends RecyclerView.ViewHolder{


        public TextView mTextViewTitle;
        public View view;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.textViewtitle);
            view = itemView.findViewById(R.id.view);

        }
    }
}

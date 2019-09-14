package com.example.dell.spotify_clone_main.youtube_files;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.spotify_clone_main.Model.Item;

import com.example.dell.spotify_clone_main.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class YoutubeAdapter extends ArrayAdapter<Item> {
    public YoutubeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Item> objects) {
        super(context, resource, objects);

    }

    class ViewHolder{
        TextView txtTen;
        ImageView imghinh;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.dong_video,null);
            viewHolder = new ViewHolder();
            viewHolder.imghinh = convertView.findViewById(R.id.imageviewThumbnail);
            viewHolder.txtTen = convertView.findViewById(R.id.textviewTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Item item = getItem(position);

        viewHolder.txtTen.setText(item.getSnippet().getTitle());
        Picasso.get().load(item.getSnippet().getThumbnails().getMedium().getUrl()).into(viewHolder.imghinh);


        return convertView;
    }

}

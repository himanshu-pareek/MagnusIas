package com.magnusias.magnusias.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magnusias.magnusias.R;
import com.magnusias.magnusias.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends ArrayAdapter<Video> {
    public VideoAdapter (Context context, ArrayList<Video> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_video_item, parent, false
            );
        }

        Video video = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.video_title_view);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.video_image_view);

        textView.setText(video.getTitle());
        Picasso.with(getContext())
                .load(video.getImageUrl())
                .fit()
                .placeholder(R.drawable.magnus_ias)
                .error(R.drawable.magnus_ias)
                .into(imageView);

        return listItemView;
    }
}

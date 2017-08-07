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
import com.magnusias.magnusias.models.CatSubject;
import com.magnusias.magnusias.models.Topic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopicAdapter extends ArrayAdapter<Topic> {
    public TopicAdapter (Context context, ArrayList<Topic> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_topic_item, parent, false
            );
        }

        Topic topic = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.topic_title_text_view);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.topic_image_view);
        TextView descView = (TextView) listItemView.findViewById(R.id.topic_desc_text_view);

        textView.setText(topic.getTitle());
        Picasso.with(getContext())
                .load(topic.getImageUrl())
                .fit()
                .placeholder(R.drawable.magnus_ias)
                .error(R.drawable.magnus_ias)
                .into(imageView);
        descView.setText(topic.getDescription());

        return listItemView;
    }
}

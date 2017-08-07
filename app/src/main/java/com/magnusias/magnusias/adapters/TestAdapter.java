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
import com.magnusias.magnusias.models.Test;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TestAdapter extends ArrayAdapter<Test> {

    public TestAdapter(Context context, ArrayList<Test> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_test_item, parent, false
            );
        }

        Test c = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.test_name_text);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.test_image_view);

        textView.setText(c.getName());
        imageView.setImageResource(R.drawable.test_active);

        return listItemView;
    }

}

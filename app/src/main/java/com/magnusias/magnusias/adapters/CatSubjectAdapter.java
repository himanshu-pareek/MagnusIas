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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CatSubjectAdapter extends ArrayAdapter<CatSubject> {

    public CatSubjectAdapter(Context context, ArrayList<CatSubject> resource) {
        super(context, 0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_cat_subject_item, parent, false
            );
        }

        CatSubject c = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.cat_subject_name_text);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.cat_subject_image_view);

        textView.setText(c.getCategory());
        Picasso.with(getContext())
                .load(c.getImageUrl())
                .fit()
                .placeholder(R.drawable.magnus_ias)
                .error(R.drawable.magnus_ias)
                .into(imageView);

        return listItemView;
    }

}

package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;

public class AboutList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final Context context;

    public AboutList(Context context) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {

        return position != 2;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_about_list_child, null);

        final ImageView listChildIconImageView = convertView.findViewById(R.id.view_about_list_child_icon_image_view);
        final TextView listChildTitleTextView = convertView.findViewById(R.id.view_about_list_child_title_text_view);

        switch (position) {

            case 0:

                listChildIconImageView.setImageResource(R.drawable.person);
                listChildTitleTextView.setText("Armin Ghofrani  •  Development");

                break;

            case 1:

                listChildIconImageView.setImageResource(R.drawable.person);
                listChildTitleTextView.setText("Oliver Rausch  •  Graphic Design");

                break;

            case 2:

                listChildIconImageView.setImageResource(R.drawable.version);
                listChildTitleTextView.setText("Version " + context.getString(R.string.app_version));

                break;

            case 3:

                listChildIconImageView.setImageResource(R.drawable.changelog);
                listChildTitleTextView.setText("View Changelog");

                break;

            case 4:

                listChildIconImageView.setImageResource(R.drawable.licenses);
                listChildTitleTextView.setText("View Licenses");

                break;

            case 5:

                listChildIconImageView.setImageResource(R.drawable.rate);
                listChildTitleTextView.setText("Rate on Google Play");

                break;

            case 6:

                listChildIconImageView.setImageResource(R.drawable.code);
                listChildTitleTextView.setText("View Source Code");

                break;

            case 7:

                listChildIconImageView.setImageResource(R.drawable.bugs);
                listChildTitleTextView.setText("Report Bugs");

                break;

            case 8:

                listChildIconImageView.setImageResource(R.drawable.features);
                listChildTitleTextView.setText("Request Features");

                break;

        }

        return convertView;

    }

}
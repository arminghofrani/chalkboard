package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.ArrayList;

public class TimetableClassList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final ArrayList<StandardClass> classesArrayList;

    public TimetableClassList(Context context, ArrayList<StandardClass> classesArrayList) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classesArrayList = classesArrayList;

    }

    public void setClassesArrayList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return classesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final StandardClass standardClass = classesArrayList.get(position);

        boolean subtitle = false;

        if (standardClass.hasLocation()) {

            convertView = layoutInflater.inflate(R.layout.view_timed_class_child_with_subtitle, parent, false);
            subtitle = true;

        } else if (standardClass.hasTeacher()) {

            convertView = layoutInflater.inflate(R.layout.view_timed_class_child_with_subtitle, parent, false);
            subtitle = true;

        } else {

            convertView = layoutInflater.inflate(R.layout.view_timed_class_child_without_subtitle, parent, false);

        }

        ((TextView) convertView.findViewById(subtitle ? R.id.view_timed_class_child_with_subtitle_title_text_view : R.id.view_timed_class_child_without_subtitle_title_text_view)).setText(standardClass.getName());

        if (subtitle) {

            if (standardClass.hasLocation()) {

                if (standardClass.hasTeacher())
                    ((TextView) convertView.findViewById(R.id.view_timed_class_child_with_subtitle_subtitle_text_view)).setText(standardClass.getLocation() + " • " + standardClass.getTeacher());
                else
                    ((TextView) convertView.findViewById(R.id.view_timed_class_child_with_subtitle_subtitle_text_view)).setText(standardClass.getLocation());

            } else {

                ((TextView) convertView.findViewById(R.id.view_timed_class_child_with_subtitle_subtitle_text_view)).setText(standardClass.getTeacher());

            }

        }

        ((TextView) convertView.findViewById(subtitle ? R.id.view_timed_class_child_with_subtitle_time_text_view : R.id.view_timed_class_child_without_subtitle_time_text_view)).setText(standardClass.getStartTimeString(true) + "\n" + standardClass.getEndTimeString(true));
        ((ImageView) convertView.findViewById(subtitle ? R.id.view_timed_class_child_with_subtitle_color_indicator_image_view : R.id.view_timed_class_child_without_subtitle_color_indicator_image_view)).setColorFilter(standardClass.getColor());

        return convertView;

    }

}
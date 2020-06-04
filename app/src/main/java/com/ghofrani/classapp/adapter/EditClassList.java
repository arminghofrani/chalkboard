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

public class EditClassList extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final ArrayList<StandardClass> classesArrayList;

    public EditClassList(Context context, ArrayList<StandardClass> classesArrayList) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classesArrayList = classesArrayList;

    }

    @Override
    public int getCount() {
        return classesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return classesArrayList.get(position);
    }

    public void setClassesArrayList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final StandardClass standardClass = classesArrayList.get(position);

        boolean subtitle = false;

        if (standardClass == null) {

            return layoutInflater.inflate(R.layout.view_edit_class_list_child_add, parent, false);

        } else if (standardClass.hasLocation()) {

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
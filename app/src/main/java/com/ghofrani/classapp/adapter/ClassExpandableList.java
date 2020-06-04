package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.model.StandardClass;

import java.util.ArrayList;

public class ClassExpandableList extends BaseExpandableListAdapter {

    private final ArrayList<StandardClass> classesArrayList;
    private final String groupTitle;
    private final LayoutInflater layoutInflater;

    public ClassExpandableList(Context context, ArrayList<StandardClass> classesArrayList, String groupTitle) {

        this.classesArrayList = classesArrayList;
        this.groupTitle = groupTitle;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void updateArrayList(ArrayList<StandardClass> update) {

        classesArrayList.clear();
        classesArrayList.addAll(update);

        notifyDataSetChanged();

    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classesArrayList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classesArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classesArrayList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_expandable_list_group, parent, false);

        final TextView listGroupTitleTextView = convertView.findViewById(R.id.view_expandable_list_group_title_text_view);
        listGroupTitleTextView.setText(groupTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.view_timed_class_child_combined, parent, false);

        final StandardClass standardClass = classesArrayList.get(childPosition);

        final RelativeLayout relativeLayout = convertView.findViewById(R.id.view_timed_class_child_combined_relative_layout);
        final TextView titleTextView = convertView.findViewById(R.id.view_timed_class_child_combined_title_text_view);
        final TextView locationTeacherTextView = convertView.findViewById(R.id.view_timed_class_child_combined_location_teacher_text_view);
        final TextView titleTextViewCentered = convertView.findViewById(R.id.view_timed_class_child_combined_title_centered_text_view);

        if (standardClass.hasLocation()) {

            titleTextViewCentered.setVisibility(View.GONE);

            relativeLayout.setVisibility(View.VISIBLE);

            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(standardClass.getName());

            locationTeacherTextView.setVisibility(View.VISIBLE);

            if (standardClass.hasTeacher())
                locationTeacherTextView.setText(standardClass.getLocation() + " • " + standardClass.getTeacher());
            else
                locationTeacherTextView.setText(standardClass.getLocation());

        } else if (standardClass.hasTeacher()) {

            titleTextViewCentered.setVisibility(View.GONE);

            relativeLayout.setVisibility(View.VISIBLE);

            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(standardClass.getName());

            locationTeacherTextView.setVisibility(View.VISIBLE);
            locationTeacherTextView.setText(standardClass.getTeacher());

        } else {

            relativeLayout.setVisibility(View.GONE);

            titleTextView.setVisibility(View.GONE);
            locationTeacherTextView.setVisibility(View.GONE);

            titleTextViewCentered.setVisibility(View.VISIBLE);
            titleTextViewCentered.setText(standardClass.getName());

        }

        final TextView listChildTimeTextView = convertView.findViewById(R.id.view_timed_class_child_combined_time_text_view);
        listChildTimeTextView.setText(standardClass.getStartTimeString(true) + "\n" + standardClass.getEndTimeString(true));

        final ImageView listChildColorIndicator = convertView.findViewById(R.id.view_timed_class_child_combined_color_indicator_image_view);
        listChildColorIndicator.setColorFilter(standardClass.getColor());

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

}
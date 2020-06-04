package com.ghofrani.classapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.adapter.TimetableClassList;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.DataSingleton;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;

public class Day extends Fragment {

    private ListView listView;
    private TimetableClassList listAdapter;
    private RelativeLayout noClassesRelativeLayout;
    private ScrollView scrollView;

    private int day;

    public static final Day newInstance(int day) {

        Day dayFragment = new Day();

        Bundle bundle = new Bundle(1);
        bundle.putInt("day", day);

        dayFragment.setArguments(bundle);

        return dayFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        day = getArguments().getInt("day");

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_day, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (listView == null)
            listView = getView().findViewById(R.id.fragment_day_list_view);

        if (noClassesRelativeLayout == null)
            noClassesRelativeLayout = getView().findViewById(R.id.fragment_day_no_classes_relative_layout);

        if (scrollView == null)
            scrollView = getView().findViewById(R.id.fragment_day_scroll_view);

        updateUI();

    }

    @Override
    public void onDestroyView() {

        listView = null;
        listAdapter = null;
        noClassesRelativeLayout = null;
        scrollView = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (getClasses() != null) {

            noClassesRelativeLayout.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            if (listAdapter == null) {

                listAdapter = new TimetableClassList(getContext(), (ArrayList<StandardClass>) getClasses().clone());
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        startActivity(new Intent(getContext(), ViewClass.class).putExtra("class", getClasses().get(position).getName()));

                    }

                });

            } else {

                listAdapter.setClassesArrayList((ArrayList<StandardClass>) getClasses().clone());

            }

            setListViewHeightBasedOnChildren();

        } else {

            scrollView.setVisibility(View.GONE);
            noClassesRelativeLayout.setVisibility(View.VISIBLE);

        }

    }

    private ArrayList<StandardClass> getClasses() {

        switch (day) {

            case DateTimeConstants.MONDAY:
                return DataSingleton.getInstance().getMondayClasses();

            case DateTimeConstants.TUESDAY:
                return DataSingleton.getInstance().getTuesdayClasses();

            case DateTimeConstants.WEDNESDAY:
                return DataSingleton.getInstance().getWednesdayClasses();

            case DateTimeConstants.THURSDAY:
                return DataSingleton.getInstance().getThursdayClasses();

            case DateTimeConstants.FRIDAY:
                return DataSingleton.getInstance().getFridayClasses();

            case DateTimeConstants.SATURDAY:
                return DataSingleton.getInstance().getSaturdayClasses();

            case DateTimeConstants.SUNDAY:
                return DataSingleton.getInstance().getSundayClasses();

            default:
                return null;

        }

    }

    private void setListViewHeightBasedOnChildren() {

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = listView.getLayoutParams();
        listViewLayoutParams.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(listViewLayoutParams);

    }

}
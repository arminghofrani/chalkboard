package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.ClassRecycler;
import com.ghofrani.classapp.module.DataSingleton;

public class Classes extends Fragment {

    private RecyclerView recyclerView;
    private ClassRecycler classRecyclerAdapter;
    private RelativeLayout noClassesRelativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_classes, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = getView().findViewById(R.id.fragment_classes_recycler_view);

        if (noClassesRelativeLayout == null)
            noClassesRelativeLayout = getView().findViewById(R.id.fragment_classes_no_classes_relative_layout);

        updateUI();

    }

    @Override
    public void onDestroyView() {

        recyclerView = null;
        classRecyclerAdapter = null;
        noClassesRelativeLayout = null;

        super.onDestroyView();

    }

    private void updateUI() {

        if (!DataSingleton.getInstance().getAllClassesArrayList().isEmpty()) {

            noClassesRelativeLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            classRecyclerAdapter = new ClassRecycler(getContext());
            recyclerView.setAdapter(classRecyclerAdapter);

        } else {

            recyclerView.setVisibility(View.GONE);
            noClassesRelativeLayout.setVisibility(View.VISIBLE);

        }

    }

}
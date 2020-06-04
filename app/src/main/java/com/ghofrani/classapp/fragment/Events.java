package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.EventRecycler;
import com.ghofrani.classapp.event.UpdateEventsUI;
import com.ghofrani.classapp.model.EventWithID;
import com.ghofrani.classapp.module.DataSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Events extends Fragment {

    private RecyclerView recyclerView;
    private EventRecycler eventRecycler;
    private LinearLayoutManager linearLayoutManager;
    private RelativeLayout noEventsRelativeLayout;
    private DividerItemDecoration dividerItemDecoration;

    @Subscribe
    public void onEvent(UpdateEventsUI updateEventsUI) {

        if (recyclerView.getVisibility() == View.VISIBLE)
            eventRecycler.dataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_events, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();

        if (recyclerView == null)
            recyclerView = getView().findViewById(R.id.fragment_events_recycler_view);

        if (noEventsRelativeLayout == null)
            noEventsRelativeLayout = getView().findViewById(R.id.fragment_events_no_events_relative_layout);

        if (linearLayoutManager == null)
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        if (dividerItemDecoration == null)
            dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);

        recyclerView.removeItemDecoration(dividerItemDecoration);

        super.onPause();

    }

    @Override
    public void onDestroyView() {

        recyclerView.setAdapter(null);

        recyclerView = null;
        eventRecycler = null;
        noEventsRelativeLayout = null;
        linearLayoutManager = null;
        dividerItemDecoration = null;

        super.onDestroyView();

    }

    private void updateUI() {

        boolean dataSetEmpty = true;

        String filter;

        if (!((AppCompatActivity) getActivity()).getClass().getSimpleName().equals("Main")) {

            filter = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();

            for (final Object object : DataSingleton.getInstance().getEventDataArrayList()) {

                if (object instanceof EventWithID)
                    if (((EventWithID) object).getEvent().getClassName().equals(filter))
                        dataSetEmpty = false;

            }

        } else {

            filter = "";

            dataSetEmpty = DataSingleton.getInstance().getEventDataArrayList().isEmpty();

        }

        if (!dataSetEmpty) {

            noEventsRelativeLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            eventRecycler = new EventRecycler(getContext(), recyclerView, filter);

            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_divider));

            recyclerView.setAdapter(eventRecycler);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(dividerItemDecoration);

        } else {

            recyclerView.setVisibility(View.GONE);
            noEventsRelativeLayout.setVisibility(View.VISIBLE);

        }

    }

}
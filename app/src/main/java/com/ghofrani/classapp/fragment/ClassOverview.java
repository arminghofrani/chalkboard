package com.ghofrani.classapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.adapter.ViewClassExpandableList;
import com.ghofrani.classapp.event.CollapseLists;
import com.ghofrani.classapp.event.UpdateClassesUI;
import com.ghofrani.classapp.model.DatedStandardClass;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;

import java.util.ArrayList;

public class ClassOverview extends Fragment {

    private final int EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE = 25;
    private final int EXPANDABLE_LIST_VIEW_HEIGHT = 40;

    private ExpandableListView expandableListViewUpcomingClasses;
    private ViewClassExpandableList viewClassListAdapterUpcoming;
    private ArrayList<DatedStandardClass> datedStandardClassArrayList;
    private String className;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_class_overview, container, false);

    }

    @Subscribe
    public void onEvent(CollapseLists collapseListsEvent) {

        if (expandableListViewUpcomingClasses != null) {

            if (expandableListViewUpcomingClasses.isGroupExpanded(0)) {

                expandableListViewUpcomingClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenUpcoming(true, false);

            }

        }

    }

    @Subscribe
    public void onEvent(UpdateClassesUI updateClassesUIEvent) {

        if (expandableListViewUpcomingClasses != null) {

            if (expandableListViewUpcomingClasses.isGroupExpanded(0)) {

                expandableListViewUpcomingClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenUpcoming(true, true);

            }

        }

        updateUI();

    }

    @Override
    public void onResume() {

        super.onResume();

        className = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();

        updateUI();

        EventBus.getDefault().register(this);

    }

    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);

        if (expandableListViewUpcomingClasses != null) {

            if (expandableListViewUpcomingClasses.isGroupExpanded(0)) {

                expandableListViewUpcomingClasses.collapseGroup(0);

                setListViewHeightBasedOnChildrenUpcoming(true, false);

            }

        }

        super.onPause();

    }

    @Override
    public void onDestroyView() {

        expandableListViewUpcomingClasses = null;
        viewClassListAdapterUpcoming = null;

        super.onDestroyView();

    }

    private void updateUI() {

        final ArrayList<DatedStandardClass> datedStandardClassArrayListLocal = getNextClasses(5, className);

        if (datedStandardClassArrayListLocal != null) {

            datedStandardClassArrayList = datedStandardClassArrayListLocal;

            configureExpandableListViewNextClasses();

            getView().findViewById(R.id.fragment_class_overview_upcoming_classes_card_view).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.fragment_class_overview_no_classes_relative_layout).setVisibility(View.GONE);

        } else {

            expandableListViewUpcomingClasses = null;

            getView().findViewById(R.id.fragment_class_overview_upcoming_classes_card_view).setVisibility(View.GONE);
            getView().findViewById(R.id.fragment_class_overview_no_classes_relative_layout).setVisibility(View.VISIBLE);

        }

    }

    private void configureExpandableListViewNextClasses() {

        if (expandableListViewUpcomingClasses == null) {

            expandableListViewUpcomingClasses = getView().findViewById(R.id.fragment_class_overview_upcoming_classes_list_view);

            expandableListViewUpcomingClasses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                public boolean onGroupClick(ExpandableListView expandableListViewGroupListener, View view, int groupPosition, long id) {

                    expandableListViewUpcomingClasses = expandableListViewGroupListener;

                    if (!expandableListViewUpcomingClasses.isGroupExpanded(0))
                        expandableListViewUpcomingClasses.expandGroup(0);
                    else
                        expandableListViewUpcomingClasses.collapseGroup(0);

                    setListViewHeightBasedOnChildrenUpcoming(true, true);

                    return true;

                }

            });

        }

        if (viewClassListAdapterUpcoming == null) {

            viewClassListAdapterUpcoming = new ViewClassExpandableList(getContext(), datedStandardClassArrayList);
            expandableListViewUpcomingClasses.setAdapter(viewClassListAdapterUpcoming);

            if (!expandableListViewUpcomingClasses.isGroupExpanded(0)) {

                expandableListViewUpcomingClasses.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (expandableListViewUpcomingClasses != null) {

                            expandableListViewUpcomingClasses.expandGroup(0);
                            setListViewHeightBasedOnChildrenUpcoming(true, true);

                        }

                    }

                }, 200);

            }

        } else {

            viewClassListAdapterUpcoming.updateArrayList((ArrayList<DatedStandardClass>) datedStandardClassArrayList.clone());

            expandableListViewUpcomingClasses.postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (expandableListViewUpcomingClasses != null) {

                        expandableListViewUpcomingClasses.expandGroup(0);
                        setListViewHeightBasedOnChildrenUpcoming(true, true);

                    }

                }

            }, 200);

        }

    }

    private ArrayList<DatedStandardClass> getNextClasses(int count, String className) {

        int day = new DateTime().getDayOfWeek();
        int iterations = -1;

        final LocalTime now = new LocalTime().withHourOfDay(LocalTime.now().getHourOfDay()).withMinuteOfHour(LocalTime.now().getMinuteOfHour()).withSecondOfMinute(0).withMillisOfSecond(0);
        final ArrayList<DatedStandardClass> returnArrayList = new ArrayList<>();

        while (returnArrayList.size() < count) {

            iterations++;

            if (Utils.getClassesArrayListOfDay(day) != null) {

                for (final StandardClass standardClass : Utils.getClassesArrayListOfDay(day)) {

                    if (standardClass.getName().equals(className)) {

                        if (iterations == 0) {

                            if (standardClass.getStartTime().isAfter(now))
                                returnArrayList.add(new DatedStandardClass(standardClass, new DateTime().plusDays(iterations).withTimeAtStartOfDay()));

                        } else {

                            returnArrayList.add(new DatedStandardClass(standardClass, new DateTime().plusDays(iterations).withTimeAtStartOfDay()));

                        }

                        if (returnArrayList.size() == count)
                            return returnArrayList;

                    }

                }

            }

            if (day == DateTimeConstants.SUNDAY)
                day = DateTimeConstants.MONDAY;
            else
                day++;

            if (iterations == 7 && returnArrayList.isEmpty())
                return null;

        }

        return returnArrayList;

    }

    private void setListViewHeightBasedOnChildrenUpcoming(boolean changeParams, boolean animate) {

        if (expandableListViewUpcomingClasses == null)
            return;

        final ListAdapter listAdapter = expandableListViewUpcomingClasses.getAdapter();

        if (listAdapter == null)
            return;

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            final View listItem = listAdapter.getView(i, null, expandableListViewUpcomingClasses);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

        }

        final ViewGroup.LayoutParams listViewLayoutParams = expandableListViewUpcomingClasses.getLayoutParams();

        if (changeParams) {

            final TextView groupText = expandableListViewUpcomingClasses.findViewById(R.id.view_expandable_list_group_title_text_view);

            if (groupText == null)
                return;

            final LinearLayout.LayoutParams groupTextLayoutParams = (LinearLayout.LayoutParams) groupText.getLayoutParams();

            if (expandableListViewUpcomingClasses.isGroupExpanded(0)) {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewUpcomingClasses.getParent();
                final int listViewLayoutParamsHeight = totalHeight + getPixelFromDP(8) + (expandableListViewUpcomingClasses.getDividerHeight() * (listAdapter.getCount() - 1));
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation expandAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + interpolatedTime * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    expandAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * datedStandardClassArrayList.size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * datedStandardClassArrayList.size() : 100);
                    parentLayout.startAnimation(expandAnimation);

                } else {

                    relativeLayoutParams.height = listViewLayoutParamsHeight;

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = listViewLayoutParamsHeight;

                groupTextLayoutParams.setMargins(0, getPixelFromDP(10), 0, getPixelFromDP(18));
                groupText.setLayoutParams(groupTextLayoutParams);

            } else {

                final RelativeLayout parentLayout = (RelativeLayout) expandableListViewUpcomingClasses.getParent();
                final int listViewLayoutParamsHeight = listViewLayoutParams.height;
                final FrameLayout.LayoutParams relativeLayoutParams = (FrameLayout.LayoutParams) parentLayout.getLayoutParams();

                if (animate) {

                    Animation collapseAnimation = new Animation() {

                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {

                            relativeLayoutParams.height = (int) (getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT) + (1 - interpolatedTime) * (listViewLayoutParamsHeight - getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT)));

                            parentLayout.requestLayout();

                        }
                    };

                    collapseAnimation.setDuration(EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * datedStandardClassArrayList.size() > 100 ? EXPANDABLE_LIST_VIEW_ANIMATION_DURATION_SCALE * datedStandardClassArrayList.size() : 100);
                    parentLayout.startAnimation(collapseAnimation);

                } else {

                    relativeLayoutParams.height = getPixelFromDP(EXPANDABLE_LIST_VIEW_HEIGHT);

                    parentLayout.requestLayout();

                }

                listViewLayoutParams.height = totalHeight + (expandableListViewUpcomingClasses.getDividerHeight() * (listAdapter.getCount() - 1));

                groupTextLayoutParams.setMargins(0, getPixelFromDP(10), 0, getPixelFromDP(10));
                groupText.setLayoutParams(groupTextLayoutParams);

            }

        } else {

            if (!expandableListViewUpcomingClasses.isGroupExpanded(0))
                listViewLayoutParams.height = totalHeight + (expandableListViewUpcomingClasses.getDividerHeight() * (listAdapter.getCount() - 1));
            else
                listViewLayoutParams.height = totalHeight + getPixelFromDP(8) + (expandableListViewUpcomingClasses.getDividerHeight() * (listAdapter.getCount() - 1));

        }

        expandableListViewUpcomingClasses.setLayoutParams(listViewLayoutParams);
        expandableListViewUpcomingClasses.requestLayout();

    }

    private int getPixelFromDP(float dPtoConvert) {

        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dPtoConvert * scale + 0.5f);

    }

}
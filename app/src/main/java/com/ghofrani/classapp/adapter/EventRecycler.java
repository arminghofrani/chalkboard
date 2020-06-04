package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ChangeEvent;
import com.ghofrani.classapp.model.Event;
import com.ghofrani.classapp.model.EventWithID;
import com.ghofrani.classapp.model.StandardClass;
import com.ghofrani.classapp.model.StringWithID;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class EventRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_HOMEWORK = 0;
    private final int VIEW_TYPE_SECTION = 1;

    private final DateTimeFormatter dayOfWeekString;
    private final DateTimeFormatter time24Hour;
    private final DateTimeFormatter timeAMPM;
    private final DateTimeFormatter shortDate;
    private final boolean is24Hour;
    private final DateTime tomorrow;
    private final View.OnClickListener onClickListener;
    private final String filter;
    private ArrayList<Object> eventDataArrayList;

    public EventRecycler(final Context context, final RecyclerView recyclerView, final String filter) {

        dayOfWeekString = DateTimeFormat.forPattern("EEEE");
        time24Hour = DateTimeFormat.forPattern("HH:mm");
        timeAMPM = DateTimeFormat.forPattern("h:mm a");
        shortDate = DateTimeFormat.forPattern("dd/MM/yy");
        is24Hour = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("24_hour_time", true);
        tomorrow = DateTime.now().plusDays(1).withTime(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour(), 0, 0);
        this.filter = filter;

        generateEventDataArrayList();

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ChangeEvent.class).putExtra("mode_edit", "true").putExtra("event_position", (int) ((long) recyclerView.getAdapter().getItemId(recyclerView.getChildAdapterPosition(view)))));

            }

        };

    }

    public void dataSetChanged() {

        generateEventDataArrayList();
        notifyDataSetChanged();

    }

    private void generateEventDataArrayList() {

        eventDataArrayList = (ArrayList<Object>) DataSingleton.getInstance().getEventDataArrayList().clone();

        if (!filter.isEmpty()) {

            for (int i = 1; i < eventDataArrayList.size(); i++) {

                if (eventDataArrayList.get(i) instanceof StringWithID)
                    continue;

                if (!((EventWithID) eventDataArrayList.get(i)).getEvent().getClassName().equals(filter)) {

                    eventDataArrayList.remove(i);
                    i--;

                }

            }

            boolean lastItemWasTitle = false;

            for (int i = eventDataArrayList.size() - 1; i >= 0; i--) {

                if (eventDataArrayList.get(i) instanceof StringWithID) {

                    if (lastItemWasTitle || i == eventDataArrayList.size() - 1)
                        eventDataArrayList.remove(i);
                    else
                        lastItemWasTitle = true;

                } else {

                    lastItemWasTitle = false;

                }

            }

        }

    }

    @Override
    public long getItemId(int position) {

        if (eventDataArrayList.get(position) instanceof StringWithID)
            return ((StringWithID) eventDataArrayList.get(position)).getID();
        else
            return ((EventWithID) eventDataArrayList.get(position)).getID();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_HOMEWORK) {

            final Event event = ((EventWithID) eventDataArrayList.get(position)).getEvent();
            final ListEventViewHolder listEventViewHolder = (ListEventViewHolder) viewHolder;

            listEventViewHolder.titleTextView.setText(event.getClassName() + " • " + event.getName());

            String subtitleTextViewText = "";

            if (event.isAttach()) {

                ArrayList<StandardClass> classesList = Utils.getClassesArrayListOfDay(event.getDateTime().getDayOfWeek());

                if (classesList != null) {

                    int index = 0;
                    boolean completed = false;

                    while (index < classesList.size() && !completed) {

                        if (classesList.get(index).getName().equals(event.getClassName())) {

                            if (classesList.get(index).getStartTime().equals(event.getDateTime().toLocalTime())) {

                                if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                                    subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                                        subtitleTextViewText = "Today • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                                    else
                                        subtitleTextViewText = "Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                                        subtitleTextViewText = "Yesterday • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                                    else
                                        subtitleTextViewText = shortDate.print(event.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                } else {

                                    subtitleTextViewText = dayOfWeekString.print(event.getDateTime()) + " • Class at " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                                }

                                completed = true;

                            }

                        }

                        index++;

                    }

                }

            } else {

                if (event.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                    subtitleTextViewText = shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    subtitleTextViewText = is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime());

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                        subtitleTextViewText = "Today • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                    else
                        subtitleTextViewText = is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime());

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                        subtitleTextViewText = "Yesterday • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));
                    else
                        subtitleTextViewText = shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else {

                    subtitleTextViewText = dayOfWeekString.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                }

            }

            if (subtitleTextViewText.isEmpty()) {

                if (event.getDateTime().isAfter(DataSingleton.getInstance().getNextWeekEnd())) {

                    listEventViewHolder.subtitleTextView.setText(shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                } else if (tomorrow.withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    listEventViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay())) {

                    if (event.getDateTime().isBefore(tomorrow.minusDays(1)) || event.getDateTime().isEqual(tomorrow.minusDays(1)))
                        listEventViewHolder.subtitleTextView.setText("Today • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));
                    else
                        listEventViewHolder.subtitleTextView.setText(is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime()));

                } else if (tomorrow.minusDays(1).withTimeAtStartOfDay().isAfter(event.getDateTime())) {

                    if (tomorrow.minusDays(2).withTimeAtStartOfDay().isEqual(event.getDateTime().withTimeAtStartOfDay()))
                        listEventViewHolder.subtitleTextView.setText("Yesterday • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));
                    else
                        listEventViewHolder.subtitleTextView.setText(shortDate.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                } else {

                    listEventViewHolder.subtitleTextView.setText(dayOfWeekString.print(event.getDateTime()) + " • " + (is24Hour ? time24Hour.print(event.getDateTime()) : timeAMPM.print(event.getDateTime())));

                }

            } else {

                listEventViewHolder.subtitleTextView.setText(subtitleTextViewText);

            }

            listEventViewHolder.colorIndicatorImageView.setColorFilter(event.getColor());

            if (event.getType() == Event.TYPE_HOMEWORK)
                listEventViewHolder.typeIndicatorTextView.setText("H");
            else if (event.getType() == Event.TYPE_TASK)
                listEventViewHolder.typeIndicatorTextView.setText("T");
            else if (event.getType() == Event.TYPE_EXAM)
                listEventViewHolder.typeIndicatorTextView.setText("E");

        } else {

            String text = ((StringWithID) eventDataArrayList.get(position)).getString();

            final ListSectionViewHolder listSectionViewHolder = (ListSectionViewHolder) viewHolder;

            listSectionViewHolder.sectionTextView.setText(text);

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (eventDataArrayList.get(position) instanceof EventWithID)
            return VIEW_TYPE_HOMEWORK;
        else
            return VIEW_TYPE_SECTION;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HOMEWORK) {

            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_recycler_event, parent, false);
            view.setOnClickListener(onClickListener);

            return new ListEventViewHolder(view);

        } else {

            return new ListSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_recycler_section, parent, false));

        }

    }

    @Override
    public int getItemCount() {
        return eventDataArrayList.size();
    }

    class ListEventViewHolder extends RecyclerView.ViewHolder {

        final FrameLayout frameLayout;
        final TextView titleTextView;
        final TextView subtitleTextView;
        final ImageView colorIndicatorImageView;
        final TextView typeIndicatorTextView;

        ListEventViewHolder(View itemView) {

            super(itemView);

            frameLayout = itemView.findViewById(R.id.view_event_recycler_event_frame_layout);
            titleTextView = itemView.findViewById(R.id.view_event_recycler_event_name_text_view);
            subtitleTextView = itemView.findViewById(R.id.view_event_recycler_event_due_text_view);
            colorIndicatorImageView = itemView.findViewById(R.id.view_event_recycler_event_color_indicator_image_view);
            typeIndicatorTextView = itemView.findViewById(R.id.view_event_recycler_event_type_indicator_text_view);

        }

    }

    class ListSectionViewHolder extends RecyclerView.ViewHolder {

        final FrameLayout frameLayout;
        final TextView sectionTextView;

        ListSectionViewHolder(View itemView) {

            super(itemView);

            frameLayout = itemView.findViewById(R.id.view_event_recycler_section_frame_layout);
            sectionTextView = itemView.findViewById(R.id.view_event_recycler_section_title_text_view);

        }

    }

}
package com.ghofrani.classapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.activity.ViewClass;
import com.ghofrani.classapp.model.SlimClass;
import com.ghofrani.classapp.module.DataSingleton;

public class ClassRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final View.OnClickListener onClickListener;
    private final int VIEW_TYPE_WITH_SUBTITLE = 0;
    private final int VIEW_TYPE_WITHOUT_SUBTITLE = 1;

    public ClassRecycler(final Context context) {

        this.context = context;

        onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ClassRecycler.this.context.startActivity(new Intent(ClassRecycler.this.context, ViewClass.class).putExtra("class", (String) view.getTag()));

            }

        };

    }

    @Override
    public int getItemCount() {

        return DataSingleton.getInstance().getAllClassesArrayList().size();

    }

    @Override
    public int getItemViewType(int position) {

        if (DataSingleton.getInstance().getAllClassesArrayList().get(position).hasLocation())
            return VIEW_TYPE_WITH_SUBTITLE;
        else if (DataSingleton.getInstance().getAllClassesArrayList().get(position).hasTeacher())
            return VIEW_TYPE_WITH_SUBTITLE;
        else
            return VIEW_TYPE_WITHOUT_SUBTITLE;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_WITH_SUBTITLE)
            return new ClassRecyclerViewHolderWithSubtitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_classes_recycler_class_with_subtitle, parent, false));
        else
            return new ClassRecyclerViewHolderWithoutSubtitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_classes_recycler_class_without_subtitle, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyclerViewViewHolder, int position) {

        final SlimClass slimClass = DataSingleton.getInstance().getAllClassesArrayList().get(position);

        if (getItemViewType(position) == VIEW_TYPE_WITH_SUBTITLE) {

            final ClassRecyclerViewHolderWithSubtitle classRecyclerViewHolderWithSubtitle = (ClassRecyclerViewHolderWithSubtitle) recyclerViewViewHolder;

            classRecyclerViewHolderWithSubtitle.titleTextView.setText(slimClass.getName());

            if (slimClass.hasLocation()) {

                if (slimClass.hasTeacher())
                    classRecyclerViewHolderWithSubtitle.locationTeacherTextView.setText(slimClass.getLocation() + " • " + slimClass.getTeacher());
                else
                    classRecyclerViewHolderWithSubtitle.locationTeacherTextView.setText(slimClass.getLocation());

            } else if (slimClass.hasTeacher()) {

                classRecyclerViewHolderWithSubtitle.locationTeacherTextView.setText(slimClass.getTeacher());

            }

            classRecyclerViewHolderWithSubtitle.colorIndicatorImageView.setColorFilter(slimClass.getColor());

            classRecyclerViewHolderWithSubtitle.cardView.setTag(slimClass.getName());
            classRecyclerViewHolderWithSubtitle.cardView.setOnClickListener(onClickListener);


        } else {

            final ClassRecyclerViewHolderWithoutSubtitle classRecyclerViewHolderWithoutSubtitle = (ClassRecyclerViewHolderWithoutSubtitle) recyclerViewViewHolder;

            classRecyclerViewHolderWithoutSubtitle.titleTextView.setText(slimClass.getName());

            classRecyclerViewHolderWithoutSubtitle.colorIndicatorImageView.setColorFilter(slimClass.getColor());

            classRecyclerViewHolderWithoutSubtitle.cardView.setTag(slimClass.getName());
            classRecyclerViewHolderWithoutSubtitle.cardView.setOnClickListener(onClickListener);

        }

    }

    class ClassRecyclerViewHolderWithSubtitle extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView titleTextView;
        final TextView locationTeacherTextView;
        final ImageView colorIndicatorImageView;

        ClassRecyclerViewHolderWithSubtitle(View itemView) {

            super(itemView);

            cardView = itemView.findViewById(R.id.view_classes_recycler_class_with_subtitle_card_view);
            titleTextView = itemView.findViewById(R.id.view_classes_recycler_class_with_subtitle_title_text_view);
            locationTeacherTextView = itemView.findViewById(R.id.view_classes_recycler_class_with_subtitle_location_teacher_text_view);
            colorIndicatorImageView = itemView.findViewById(R.id.view_classes_recycler_class_with_subtitle_color_indicator_image_view);

        }

    }

    class ClassRecyclerViewHolderWithoutSubtitle extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView titleTextView;
        final ImageView colorIndicatorImageView;

        ClassRecyclerViewHolderWithoutSubtitle(View itemView) {

            super(itemView);

            cardView = itemView.findViewById(R.id.view_classes_recycler_class_without_subtitle_card_view);
            titleTextView = itemView.findViewById(R.id.view_classes_recycler_class_without_subtitle_title_text_view);
            colorIndicatorImageView = itemView.findViewById(R.id.view_classes_recycler_class_without_subtitle_color_indicator_image_view);

        }

    }

}
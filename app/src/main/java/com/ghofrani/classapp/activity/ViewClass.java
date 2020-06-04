package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.fragment.ClassOverview;
import com.ghofrani.classapp.fragment.Events;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.DatabaseHelper;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;

public class ViewClass extends AppCompatActivity {

    private final int MODE_EDIT = 1;
    private final int CHANGE_CLASS_REQUEST = 0;
    private final int RESULT_CHANGED = 0;

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean floatingActionButtonContrast = false;

    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        floatingActionButtonContrast = Utils.setThemeGetContrastFlag(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        className = getIntent().getStringExtra("class");

        toolbar = findViewById(R.id.activity_view_class_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(className);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.activity_view_class_tab_layout);
        viewPager = findViewById(R.id.activity_view_class_view_pager);

        floatingActionButton = findViewById(R.id.activity_view_class_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DataSingleton.getInstance().getAllClassNamesArrayList().remove(className);
                DataSingleton.getInstance().getAllClassNamesArrayList().add(0, className);

                startActivity(new Intent(ViewClass.this, ChangeEvent.class));

            }

        });

        final com.ghofrani.classapp.adapter.ViewPager viewPagerAdapter = new com.ghofrani.classapp.adapter.ViewPager(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ClassOverview(), "CLASSES");
        viewPagerAdapter.addFragment(new Events(), "EVENTS");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {

                    if (floatingActionButton.isShown())
                        floatingActionButton.hide();

                } else {

                    if (!floatingActionButton.isShown()) {

                        floatingActionButton.show();

                        if (floatingActionButtonContrast)
                            floatingActionButton.setImageResource(R.drawable.add_black);
                        else
                            floatingActionButton.setImageResource(R.drawable.add_white);

                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

    }

    @Override
    protected void onResume() {

        super.onResume();

        performOnResume();

    }

    private void performOnResume() {

        if (toolbar == null)
            toolbar = findViewById(R.id.activity_view_class_toolbar);

        if (floatingActionButton == null)
            floatingActionButton = findViewById(R.id.activity_view_class_floating_action_button);

        if (tabLayout == null)
            tabLayout = findViewById(R.id.activity_view_class_tab_layout);

        if (viewPager == null)
            viewPager = findViewById(R.id.activity_view_class_view_pager);

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            toolbar = null;
            floatingActionButton = null;

            tabLayout = null;
            viewPager = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == android.R.id.home) {

            super.onBackPressed();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_delete_edit_delete_item) {

            final MaterialDialog.Builder materialDialogBuilder = new MaterialDialog.Builder(this);

            materialDialogBuilder.title("Delete class?");
            materialDialogBuilder.content("All timetable entries and event will be deleted for this class.");
            materialDialogBuilder.positiveText("YES");
            materialDialogBuilder.positiveColorRes(R.color.black);
            materialDialogBuilder.negativeText("CANCEL");
            materialDialogBuilder.negativeColorRes(R.color.black);

            materialDialogBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                    DatabaseHelper databaseHelper = new DatabaseHelper(ViewClass.this);

                    try {

                        databaseHelper.deleteClass(className);

                    } finally {

                        databaseHelper.close();

                    }

                    EventBus.getDefault().post(new Update(true, true, true, true, false));

                    ViewClass.super.onBackPressed();

                }

            });

            materialDialogBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction which) {

                    materialDialog.dismiss();

                }

            });

            materialDialogBuilder.show();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_delete_edit_edit_item) {

            startActivityForResult(new Intent(ViewClass.this, ChangeClass.class).putExtra("mode", MODE_EDIT).putExtra("class", className), CHANGE_CLASS_REQUEST);

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (requestCode == CHANGE_CLASS_REQUEST) {

            if (resultCode == RESULT_CHANGED) {

                className = resultIntent.getStringExtra("class_name");

                if (toolbar == null)
                    toolbar = findViewById(R.id.activity_view_class_toolbar);

                toolbar.setTitle(className);

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_delete_edit, menu);

        return true;

    }

}
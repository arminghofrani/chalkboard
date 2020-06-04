package com.ghofrani.classapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ghofrani.classapp.R;
import com.ghofrani.classapp.event.CollapseLists;
import com.ghofrani.classapp.event.Update;
import com.ghofrani.classapp.fragment.Classes;
import com.ghofrani.classapp.fragment.Day;
import com.ghofrani.classapp.fragment.Events;
import com.ghofrani.classapp.fragment.Overview;
import com.ghofrani.classapp.module.DataSingleton;
import com.ghofrani.classapp.module.Utils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class Main extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private final int CHANGE_CLASS_REQUEST = 0;
    private final int RESULT_CHANGED = 0;
    private final int ID_OVERVIEW = 0;
    private final int ID_CLASSES = 2;
    private final int ID_TIMETABLE = 3;
    private final int ID_SETTINGS = 4;
    private final int ID_ABOUT = 5;
    private final int ID_EVENTS = 6;
    private final int MODE_ADD = 0;

    private FloatingActionButton floatingActionButton;
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private TabLayout overviewTabLayout;
    private ViewPager overviewViewPager;

    private TabLayout timetableTabLayout;
    private ViewPager timetableViewPager;

    private int currentView = ID_OVERVIEW;
    private boolean operateOnDrawerClosed;
    private int drawerViewToSwitchTo;
    private boolean floatingActionButtonContrast = false;
    private int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        floatingActionButtonContrast = Utils.setThemeGetContrastFlag(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Overview");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_drawer_layout);

        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(this);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        linearLayout = findViewById(R.id.activity_main_linear_layout);

        overviewTabLayout = findViewById(R.id.activity_main_overview_tab_layout);
        overviewViewPager = findViewById(R.id.activity_main_overview_view_pager);

        timetableTabLayout = findViewById(R.id.activity_main_timetable_tab_layout);
        timetableViewPager = findViewById(R.id.activity_main_timetable_view_pager);

        navigationView = findViewById(R.id.activity_main_navigation_view);
        navigationView.setCheckedItem(R.id.drawer_overview_item);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();

                if (!menuItem.isChecked()) {

                    operateOnDrawerClosed = true;

                    if (menuItem.getItemId() == R.id.drawer_settings_item) {

                        if (currentView == ID_OVERVIEW)
                            EventBus.getDefault().post(new CollapseLists());

                        drawerViewToSwitchTo = ID_SETTINGS;

                    } else if (menuItem.getItemId() == R.id.drawer_about_item) {

                        if (currentView == ID_OVERVIEW)
                            EventBus.getDefault().post(new CollapseLists());

                        drawerViewToSwitchTo = ID_ABOUT;

                    } else {

                        switch (menuItem.getItemId()) {

                            case R.id.drawer_overview_item:

                                if (floatingActionButton.isShown())
                                    floatingActionButton.hide();

                                toolbar.setTitle("Overview");

                                linearLayout.setVisibility(View.GONE);

                                navigationView.setCheckedItem(R.id.drawer_overview_item);

                                currentView = ID_OVERVIEW;
                                drawerViewToSwitchTo = ID_OVERVIEW;

                                break;

                            case R.id.drawer_classes_item:

                                if (!floatingActionButton.isShown())
                                    floatingActionButton.show();

                                if (floatingActionButtonContrast)
                                    floatingActionButton.setImageResource(R.drawable.add_black);
                                else
                                    floatingActionButton.setImageResource(R.drawable.add_white);

                                toolbar.setTitle("Classes");

                                overviewTabLayout.setVisibility(View.GONE);
                                overviewViewPager.setVisibility(View.GONE);

                                timetableTabLayout.setVisibility(View.GONE);
                                timetableViewPager.setVisibility(View.GONE);

                                linearLayout.setVisibility(View.VISIBLE);

                                navigationView.setCheckedItem(R.id.drawer_classes_item);

                                currentView = ID_CLASSES;
                                drawerViewToSwitchTo = ID_CLASSES;

                                break;

                            case R.id.drawer_timetable_item:

                                if (floatingActionButton.isShown())
                                    floatingActionButton.hide();

                                toolbar.setTitle("Timetable");

                                linearLayout.setVisibility(View.GONE);

                                navigationView.setCheckedItem(R.id.drawer_timetable_item);

                                currentView = ID_TIMETABLE;
                                drawerViewToSwitchTo = ID_TIMETABLE;

                                break;

                        }

                    }

                }

                return true;

            }

        });

        floatingActionButton = findViewById(R.id.activity_main_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (currentView == ID_OVERVIEW) {

                    if (DataSingleton.getInstance().getAllClassNamesArrayList().isEmpty()) {

                        Toast.makeText(Main.this, "Add a class first!", Toast.LENGTH_LONG).show();

                        startActivityForResult(new Intent(Main.this, ChangeClass.class).putExtra("mode", MODE_ADD), CHANGE_CLASS_REQUEST);

                    } else {

                        startActivity(new Intent(Main.this, ChangeEvent.class));

                    }

                } else if (currentView == ID_CLASSES) {

                    startActivityForResult(new Intent(Main.this, ChangeClass.class).putExtra("mode", MODE_ADD), CHANGE_CLASS_REQUEST);

                }

            }

        });

        switchToView((getIntent().hasExtra("fragment") ? getIntent().getIntExtra("fragment", ID_OVERVIEW) : ID_OVERVIEW));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.toolbar_edit_edit_item) {

            if (!DataSingleton.getInstance().getAllClassNamesArrayList().isEmpty()) {

                startActivity(new Intent(Main.this, EditDay.class).putExtra("day", DataSingleton.getInstance().getSelectedTabPosition()));

            } else {

                Toast.makeText(Main.this, "Add a class first!", Toast.LENGTH_LONG).show();

                startActivityForResult(new Intent(Main.this, ChangeClass.class).putExtra("mode", MODE_ADD), CHANGE_CLASS_REQUEST);

            }

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_toggle_notification_off_item) {

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            sharedPreferences.edit().putBoolean("class_notification", true).commit();
            sharedPreferences.edit().putBoolean("next_class_notification", true).commit();

            EventBus.getDefault().post(new Update(true, false, false, false, false));

            invalidateOptionsMenu();

            return true;

        } else if (menuItem.getItemId() == R.id.toolbar_toggle_notification_on_item) {

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            sharedPreferences.edit().putBoolean("class_notification", false).commit();
            sharedPreferences.edit().putBoolean("next_class_notification", false).commit();

            EventBus.getDefault().post(new Update(true, false, false, false, false));

            invalidateOptionsMenu();

            return true;

        } else {

            return super.onOptionsItemSelected(menuItem);

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        performOnResume();

    }

    private void performRecreate() {

        DataSingleton.getInstance().setRecreate(false);
        DataSingleton.getInstance().setChangedFirstDay(false);
        DataSingleton.getInstance().setSelectedTabPosition(0);

        drawerLayout = null;
        toolbar = null;
        linearLayout = null;
        navigationView = null;
        floatingActionButton = null;

        overviewTabLayout = null;
        overviewViewPager = null;

        timetableTabLayout = null;
        timetableViewPager = null;

        finish();

        startActivity(getIntent());

    }

    @Override
    public void onTrimMemory(int level) {

        if (level == TRIM_MEMORY_UI_HIDDEN) {

            drawerLayout = null;
            toolbar = null;
            linearLayout = null;
            navigationView = null;
            floatingActionButton = null;

            overviewTabLayout = null;
            overviewViewPager = null;

            timetableTabLayout = null;
            timetableViewPager = null;

        }

        super.onTrimMemory(level);

    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {

        if (operateOnDrawerClosed) {

            performExpensiveSwitchOperations(drawerViewToSwitchTo, getSupportFragmentManager().beginTransaction());

            operateOnDrawerClosed = false;

        }

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (currentView == ID_OVERVIEW) {

            if (tabPosition == 0) {

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                if (sharedPreferences.getBoolean("class_notification", true) && sharedPreferences.getBoolean("next_class_notification", true))
                    getMenuInflater().inflate(R.menu.toolbar_toggle_notifications_on, menu);
                else
                    getMenuInflater().inflate(R.menu.toolbar_toggle_notifications_off, menu);

            }

        } else if (currentView == ID_TIMETABLE) {

            getMenuInflater().inflate(R.menu.toolbar_edit, menu);

        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (requestCode == CHANGE_CLASS_REQUEST) {

            if (resultCode == RESULT_CHANGED) {

                performOnResume();

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                if (sharedPreferences.getBoolean("show_toast_add_to_timetable", true)) {

                    switchToView(resultIntent.getIntExtra("switch_to_timetable", requestCode));
                    Toast.makeText(this, "Add your class into the timetable!", Toast.LENGTH_LONG).show();

                    sharedPreferences.edit().putBoolean("show_toast_add_to_timetable", false).commit();

                }

            }

        }

    }

    private void switchToView(int viewToSwitchTo) {

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (viewToSwitchTo) {

            case ID_OVERVIEW:

                if (floatingActionButton.isShown())
                    floatingActionButton.hide();

                toolbar.setTitle("Overview");

                linearLayout.setVisibility(View.GONE);

                navigationView.setCheckedItem(R.id.drawer_overview_item);

                currentView = viewToSwitchTo;

                break;

            case ID_CLASSES:

                if (!floatingActionButton.isShown())
                    floatingActionButton.show();

                if (floatingActionButtonContrast)
                    floatingActionButton.setImageResource(R.drawable.add_black);
                else
                    floatingActionButton.setImageResource(R.drawable.add_white);

                toolbar.setTitle("Classes");

                overviewTabLayout.setVisibility(View.GONE);
                overviewViewPager.setVisibility(View.GONE);

                timetableTabLayout.setVisibility(View.GONE);
                timetableViewPager.setVisibility(View.GONE);

                linearLayout.setVisibility(View.VISIBLE);

                navigationView.setCheckedItem(R.id.drawer_classes_item);

                currentView = viewToSwitchTo;

                break;

            case ID_TIMETABLE:

                if (floatingActionButton.isShown())
                    floatingActionButton.hide();

                toolbar.setTitle("Timetable");

                linearLayout.setVisibility(View.GONE);

                navigationView.setCheckedItem(R.id.drawer_timetable_item);

                currentView = viewToSwitchTo;

                break;

            case ID_EVENTS:

                if (floatingActionButton.isShown())
                    floatingActionButton.hide();

                toolbar.setTitle("Overview");

                linearLayout.setVisibility(View.GONE);

                navigationView.setCheckedItem(R.id.drawer_overview_item);

                currentView = viewToSwitchTo;

                break;

            default:

                if (currentView == ID_OVERVIEW)
                    EventBus.getDefault().post(new CollapseLists());

                break;

        }

        performExpensiveSwitchOperations(viewToSwitchTo, fragmentTransaction);

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if (intent.hasExtra("fragment")) {

            if (intent.getIntExtra("fragment", ID_OVERVIEW) != currentView) {

                performOnResume();

                switchToView(intent.getIntExtra("fragment", ID_OVERVIEW));

            }

        }

        setIntent(intent);

    }

    private void performOnResume() {

        if (toolbar == null)
            toolbar = findViewById(R.id.activity_main_toolbar);

        if (floatingActionButton == null)
            floatingActionButton = findViewById(R.id.activity_main_floating_action_button);

        if (navigationView == null)
            navigationView = findViewById(R.id.activity_main_navigation_view);

        if (drawerLayout == null)
            drawerLayout = findViewById(R.id.main_drawer_layout);

        if (overviewTabLayout == null)
            overviewTabLayout = findViewById(R.id.activity_main_overview_tab_layout);

        if (overviewViewPager == null)
            overviewViewPager = findViewById(R.id.activity_main_overview_view_pager);

        if (timetableTabLayout == null)
            timetableTabLayout = findViewById(R.id.activity_main_timetable_tab_layout);

        if (timetableViewPager == null)
            timetableViewPager = findViewById(R.id.activity_main_timetable_view_pager);

        if (linearLayout == null)
            linearLayout = findViewById(R.id.activity_main_linear_layout);

        if (DataSingleton.getInstance().isRecreate())
            performRecreate();
        else if (DataSingleton.getInstance().isChangedFirstDay())
            performRecreate();

        invalidateOptionsMenu();

    }

    private void performExpensiveSwitchOperations(int viewID, final FragmentTransaction fragmentTransaction) {

        switch (viewID) {

            case ID_OVERVIEW:

                timetableTabLayout.setVisibility(View.GONE);
                timetableViewPager.setVisibility(View.GONE);

                overviewTabLayout.clearOnTabSelectedListeners();
                overviewTabLayout.removeAllTabs();

                overviewTabLayout.setVisibility(View.VISIBLE);
                overviewViewPager.setVisibility(View.VISIBLE);

                if (getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout) != null)
                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout)).commitAllowingStateLoss();

                final com.ghofrani.classapp.adapter.ViewPager overviewAdapter = new com.ghofrani.classapp.adapter.ViewPager(getSupportFragmentManager());

                overviewAdapter.addFragment(new Overview(), "CLASSES");
                overviewAdapter.addFragment(new Events(), "EVENTS");

                overviewViewPager.setAdapter(overviewAdapter);
                overviewTabLayout.setupWithViewPager(overviewViewPager);
                overviewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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

                        tabPosition = tab.getPosition();

                        invalidateOptionsMenu();

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }

                });

                tabPosition = 0;

                invalidateOptionsMenu();

                break;

            case ID_CLASSES:

                final Classes classesFragment = new Classes();
                fragmentTransaction.replace(R.id.activity_main_linear_layout, classesFragment, "classes_fragment");
                fragmentTransaction.commit();

                invalidateOptionsMenu();

                break;

            case ID_TIMETABLE:

                overviewTabLayout.setVisibility(View.GONE);
                overviewViewPager.setVisibility(View.GONE);

                timetableTabLayout.clearOnTabSelectedListeners();
                timetableTabLayout.removeAllTabs();

                timetableTabLayout.setVisibility(View.VISIBLE);
                timetableViewPager.setVisibility(View.VISIBLE);

                if (getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout) != null)
                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout)).commitAllowingStateLoss();

                final com.ghofrani.classapp.adapter.ViewPager timetableAdapter = new com.ghofrani.classapp.adapter.ViewPager(getSupportFragmentManager());

                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                int day = Integer.parseInt(sharedPreferences.getString("first_day_of_week", "1"));

                for (int i = 0; i < 7; i++) {

                    if (day + i < 8)
                        timetableAdapter.addFragment(Day.newInstance(day + i), DateTimeFormat.forPattern("EEEE").print(new LocalDate().withDayOfWeek(day + i)).toUpperCase());
                    else
                        timetableAdapter.addFragment(Day.newInstance(day + i - 7), DateTimeFormat.forPattern("EEEE").print(new LocalDate().withDayOfWeek(day + i - 7)).toUpperCase());

                }

                timetableViewPager.setAdapter(timetableAdapter);
                timetableTabLayout.setupWithViewPager(timetableViewPager);
                timetableTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        DataSingleton.getInstance().setSelectedTabPosition(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }

                });

                invalidateOptionsMenu();

                break;

            case ID_EVENTS:

                timetableTabLayout.setVisibility(View.GONE);
                timetableViewPager.setVisibility(View.GONE);

                overviewTabLayout.clearOnTabSelectedListeners();
                overviewTabLayout.removeAllTabs();

                overviewTabLayout.setVisibility(View.VISIBLE);
                overviewViewPager.setVisibility(View.VISIBLE);

                if (getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout) != null)
                    fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.activity_main_linear_layout)).commitAllowingStateLoss();

                final com.ghofrani.classapp.adapter.ViewPager eventsAdapter = new com.ghofrani.classapp.adapter.ViewPager(getSupportFragmentManager());

                eventsAdapter.addFragment(new Overview(), "CLASSES");
                eventsAdapter.addFragment(new Events(), "EVENTS");

                overviewViewPager.setAdapter(eventsAdapter);
                overviewTabLayout.setupWithViewPager(overviewViewPager);
                overviewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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

                        tabPosition = tab.getPosition();

                        invalidateOptionsMenu();

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }

                });

                overviewViewPager.setCurrentItem(1);

                currentView = ID_OVERVIEW;

                tabPosition = 1;

                invalidateOptionsMenu();

                break;

            case ID_SETTINGS:

                startActivity(new Intent(this, Settings.class));

                break;

            case ID_ABOUT:

                startActivity(new Intent(this, About.class));

                break;

        }

    }

}
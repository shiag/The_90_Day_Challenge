package com.sgmasterappsgmail.The90DayChallenge.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sgmasterappsgmail.The90DayChallenge.R;
import com.sgmasterappsgmail.The90DayChallenge.Tools.Alarm;
import com.sgmasterappsgmail.The90DayChallenge.Tools.MySharedPref;
import com.sgmasterappsgmail.The90DayChallenge.adapters.TodoDailyAdepter;
import com.sgmasterappsgmail.The90DayChallenge.data.DailyTodoContentProvider;
import com.sgmasterappsgmail.The90DayChallenge.data.MySqlHelper;
import com.sgmasterappsgmail.The90DayChallenge.models.TodoDaily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    // private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TodoDailyAdepter todayAdepter;
    private TodoDailyAdepter archiveAdepter;
    private TodoDailyAdepter helpAdepter;
    private TodoDailyAdepter notDoneAdepter;
    private int tabCount;
    private Set<String> sendToTom;
    public static final int TODAY_LAYOUT = 0;
    public static final int ARCHIVE_LAYOUT = 1;
    public static final int HELP_LAYOUT = 2;
    public static final int NOT_DONE_LAYOUT = 3;
    private static final int LOADER_TODAY = 0;
    private static final int LOADER_TOM = 1;
    private static final int LOADER_ARCHIVE = 2;
    private static final int LOADER_HELP = 3;
    private static final int LOADER_NOT_DONE = 4;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getTracker();
        setContentView(R.layout.activity_main);
        if (MySharedPref.getBoolSharedPref(this, MySharedPref.CHECK_APPS_FIRST_LAUNCH, true)) {
            setUpAlarms();
            MySharedPref.putBoolSharedPref(this, MySharedPref.CHECK_APPS_FIRST_LAUNCH, false);
        }
        setUpGui();
        getSupportLoaderManager().initLoader(LOADER_TODAY, null, this);
        getSupportLoaderManager().initLoader(LOADER_ARCHIVE, null, this);
        getSupportLoaderManager().initLoader(LOADER_HELP, null, this);
        getSupportLoaderManager().initLoader(LOADER_NOT_DONE, null, this);
    }

    private void setUpAlarms() {
        Thread alarmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Alarm.createAlarmForDay(MainActivity.this);
                Alarm.createAlarmForNight(MainActivity.this);
                Alarm.createAlarmForWeek(MainActivity.this);
                Alarm.checkForNewDay(MainActivity.this);
            }
        }
        );
        alarmThread.start();
        for (int i = 0; i < 3; i++) {
            TodoDaily daily = new TodoDaily(i + 1);
            getContentResolver().insert(DailyTodoContentProvider.CONTENT_URI, daily.toContentValues());
        }
        for (int i = 0; i < 3; i++) {
            TodoDaily daily = new TodoDaily(i + 1);
            daily.setDate(System.currentTimeMillis() + 86400000);
            getContentResolver().insert(DailyTodoContentProvider.CONTENT_URI, daily.toContentValues());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Drawable drawable = menu.findItem(R.id.action_settings).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_do_today) {
            viewPager.setCurrentItem(TODAY_LAYOUT);
        } else if (id == R.id.nav_archive) {
            viewPager.setCurrentItem(ARCHIVE_LAYOUT);
        } else if (id == R.id.nav_help) {
            viewPager.setCurrentItem(HELP_LAYOUT);
        } else if (id == R.id.nav_not_done) {
            viewPager.setCurrentItem(NOT_DONE_LAYOUT);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(MainActivity.this, StatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            MySharedPref.sendEmail(MainActivity.this);
        } else if (id == R.id.nav_link) {
            Intent webSite = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.askmaurice.com/"));
            startActivity(webSite);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUpGui() {
        //set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // set up drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //set up navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // set up viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            MyPagerAdepter adapter = new MyPagerAdepter();
            viewPager.setAdapter(adapter);

            if (getIntent().getBooleanExtra("notdone", false)) {
                // it comes from notDone notification
                viewPager.setCurrentItem(3);
            } else {
                viewPager.setCurrentItem(0);
            }
            viewPager.setOffscreenPageLimit(4);
        }
        //set up tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setIconForTabs(tabLayout);
        setUpAdepter();
    }

    private void setUpAdepter() {
        todayAdepter = new TodoDailyAdepter(MainActivity.this, TodoDailyAdepter.TODAY, new TodoDailyAdepter.ItemClickListener() {
            @Override
            public void onItemClicked(TodoDaily daily, int icon) {
                switch (icon) {
                    case 0:
                        break;
                    case TodoDailyAdepter.EDIT:
                        setPopUpForEdit(daily);
                        break;
                    case TodoDailyAdepter.DONE:
                        setPopUpForDone(daily);
                        break;
                    case TodoDailyAdepter.HELP_B:
                        setPopUpForHelp(daily);
                        break;
                    case TodoDailyAdepter.NOT_DONE_B:
                        setPopUpForNotDone(daily);
                        break;
                    case TodoDailyAdepter.SHIFT:
                        setPopUpForShift(daily);
                        break;
                    default:
                        break;
                }
            }
        });
        archiveAdepter = new TodoDailyAdepter(MainActivity.this, TodoDailyAdepter.ARCHIVE, new TodoDailyAdepter.ItemClickListener() {
            @Override
            public void onItemClicked(TodoDaily daily, int icon) {
                switch (icon) {
                    case 0:
                        break;
                    case TodoDailyAdepter.DONE:
                        setPopUpForDone(daily);
                        break;
                    case TodoDailyAdepter.HELP_B:
                        setPopUpForHelp(daily);
                        break;
                    case TodoDailyAdepter.NOT_DONE_B:
                        setPopUpForNotDone(daily);
                        break;
                    case TodoDailyAdepter.SHIFT:
                        setPopUpForShift(daily);
                        break;
                    case TodoDailyAdepter.UP:
                        daily.setIsHide(false);
                        archiveAdepter.notifyDataSetChanged();
                        break;
                    case TodoDailyAdepter.DOWN:
                        daily.setIsHide(true);
                        archiveAdepter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        helpAdepter = new TodoDailyAdepter(MainActivity.this, TodoDailyAdepter.HELP, new TodoDailyAdepter.ItemClickListener() {
            @Override
            public void onItemClicked(TodoDaily daily, int icon) {
                switch (icon) {
                    case 0:
                        break;
                    case TodoDailyAdepter.DONE:
                        setPopUpForDone(daily);
                        break;
                    case TodoDailyAdepter.NOT_DONE_B:
                        setPopUpForNotDone(daily);
                        break;
                    case TodoDailyAdepter.SHIFT:
                        setPopUpForShift(daily);
                        break;
                    case TodoDailyAdepter.UP:
                        daily.setIsHide(false);
                        helpAdepter.notifyDataSetChanged();
                        break;
                    case TodoDailyAdepter.DOWN:
                        daily.setIsHide(true);
                        helpAdepter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        notDoneAdepter = new TodoDailyAdepter(MainActivity.this, TodoDailyAdepter.NOT_DONE, new TodoDailyAdepter.ItemClickListener() {
            @Override
            public void onItemClicked(TodoDaily daily, int icon) {
                switch (icon) {
                    case 0:
                        break;
                    case TodoDailyAdepter.DONE:
                        setPopUpForDone(daily);
                        break;
                    case TodoDailyAdepter.HELP_B:
                        setPopUpForHelp(daily);
                        break;
                    case TodoDailyAdepter.SHIFT:
                        setPopUpForShift(daily);
                        break;
                    case TodoDailyAdepter.UP:
                        daily.setIsHide(false);
                        notDoneAdepter.notifyDataSetChanged();
                        break;
                    case TodoDailyAdepter.DOWN:
                        daily.setIsHide(true);
                        notDoneAdepter.notifyDataSetChanged();
                    default:
                        break;
                }
            }
        });

    }

    private void setIconForTabs(TabLayout tabLayout) {
        Drawable drawable;
        for (int i = 0; i < tabCount; i++) {
            drawable = ContextCompat.getDrawable(this, getIconsForTabs(i));
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
            tabLayout.getTabAt(i).setIcon(drawable);
            tabLayout.getTabAt(i).setText("");
        }

    }

    public int getIconsForTabs(int i) {
        int[] imageResId = {
                R.drawable.ic_action_content_create,
                R.drawable.ic_action_av_my_library_books,
                R.drawable.ic_action_maps_location_history,
                R.drawable.ic_action_content_report
        };
        return imageResId[i];
    }

    public void setPopUpForEdit(final TodoDaily daily) {
        LayoutInflater inflater = this.getLayoutInflater();
        View editDialogView = inflater.inflate(R.layout.edit, null);
        final EditText tittleEdit = (EditText) editDialogView.findViewById(R.id.tittle_edit);
        final EditText descriptionEdit = (EditText) editDialogView.findViewById(R.id.description_edit);
        tittleEdit.setText(daily.getTittle());
        descriptionEdit.setText(daily.getDescription());
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Edit Your Goal");
        builder.setView(editDialogView);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(DailyTodoContentProvider.CONTENT_URI + "/" + daily.getId());
                daily.setTittle(tittleEdit.getText().toString());
                daily.setDescription(descriptionEdit.getText().toString());
                getContentResolver().update(uri, daily.toContentValues(), null, null);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    public void setPopUpForDone(final TodoDaily daily) {
        //Log.d(TAG, "done");
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("Done ?")
                .setConfirmText("Yes!")
                .setCancelText("Cancel")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Uri uri = Uri.parse(DailyTodoContentProvider.CONTENT_URI + "/" + daily.getId());
                        daily.setHelp(false);
                        daily.setDone(true);
                        daily.setNotDone(false);
                        getContentResolver().update(uri, daily.toContentValues(), null, null);

                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    public void setPopUpForHelp(final TodoDaily daily) {
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this);
        pDialog.setTitleText("Help!")
                .setContentText(" Do you need help to complete this Goal?")
                .setConfirmText("Yes!")
                .setCancelText("Cancel")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Uri uri = Uri.parse(DailyTodoContentProvider.CONTENT_URI + "/" + daily.getId());
                        daily.setHelp(true);
                        daily.setDone(false);
                        daily.setNotDone(false);
                        getContentResolver().update(uri, daily.toContentValues(), null, null);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    public void setPopUpForNotDone(final TodoDaily daily) {
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("Not Done!");
        pDialog.setContentText("Are you sure you don't want to finish this goal?");
        pDialog.setConfirmText("Yes!");
        pDialog.setCancelText("Cancel");
        pDialog.showCancelButton(true);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                Uri uri = Uri.parse(DailyTodoContentProvider.CONTENT_URI + "/" + daily.getId());
                daily.setHelp(false);
                daily.setDone(false);
                daily.setNotDone(true);
                getContentResolver().update(uri, daily.toContentValues(), null, null);
                sDialog.dismissWithAnimation();
            }
        })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    public void setPopUpForShift(final TodoDaily daily) {
        //Log.d(TAG, "Shift");
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this);
        pDialog
                .setTitleText("Shift!")
                .setContentText("Do You want to move over this Goal for tomorrow?")
                .setConfirmText("Yes!")
                .setCancelText("Cancel")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sendToTom = new HashSet<>(MySharedPref.getSetSharedPref(MainActivity.this, MySharedPref.SET_OF_SHIFT, new HashSet<String>()));
                        if (daily.getTittle() != null || daily.getDescription() != null) {
                            sendToTom.add(daily.getTittle() + " " + "," + " " + daily.getDescription());
                            MySharedPref.putSetSharedPref(MainActivity.this, MySharedPref.SET_OF_SHIFT, sendToTom);
                            // have to restartLoader because in this loader i called data.close it should not run by any other changes
                            getSupportLoaderManager().restartLoader(LOADER_TOM, null, MainActivity.this);
                        }
                        sDialog.dismissWithAnimation();
                    }
                })

                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String today = MySqlHelper.COLUMN_ID + " DESC LIMIT 6";
        String tom = MySqlHelper.COLUMN_ID + " DESC LIMIT 3";
        String whereHelp = MySqlHelper.COLUMN_HELP + " = ? " + " AND " + MySqlHelper.COLUMN_DONE + " = ? " + " AND " + MySqlHelper.COLUMN_NOT_DONE + " = ? ";
        String[] args1 = new String[]{"1", "0", "0"};
        String whereNotDone = MySqlHelper.COLUMN_NOT_DONE + "= ? " + " AND " + MySqlHelper.COLUMN_DONE + " = ? " + " AND " + MySqlHelper.COLUMN_HELP + " = ? ";
        String[] args2 = new String[]{"1", "0", "0"};
        switch (id) {
            case LOADER_TODAY:
                return new CursorLoader(this, DailyTodoContentProvider.CONTENT_URI,
                        null, null, null, today);
            case LOADER_TOM:
                return new CursorLoader(this, DailyTodoContentProvider.CONTENT_URI,
                        null, null, null, tom);
            case LOADER_ARCHIVE:
                return new CursorLoader(this, DailyTodoContentProvider.CONTENT_URI,
                        null, null, null, null);
            case LOADER_HELP:
                return new CursorLoader(this, DailyTodoContentProvider.CONTENT_URI,
                        null, whereHelp, args1, null);
            case LOADER_NOT_DONE:
                return new CursorLoader(this, DailyTodoContentProvider.CONTENT_URI,
                        null, whereNotDone, args2, null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_TODAY:
                if (data != null) {
                    //Log.d(TAG, "We have " + data.getCount() + " items in today");
                    if (data.moveToFirst()) {
                        List<TodoDaily> items = new ArrayList<>();
                        do {
                            items.add(TodoDaily.buildFromCursor(data));

                        } while (data.moveToNext());

                        todayAdepter.swapToday(items);
                    }
                }
                break;
            case LOADER_TOM:
                String[] goal;
                String d;
                if (data != null) {
                    //Log.d(TAG, "We have " + data.getCount() + " items in tom");
                    if (data.moveToFirst()) {
                        List<TodoDaily> items = new ArrayList<>();
                        int j = 0;
                        do {
                            items.add(TodoDaily.buildFromCursor(data));
                            TodoDaily daily = items.get(j);
                            j++;
                            if (daily.getTittle() == null && daily.getDescription() == null) {
                                sendToTom = new HashSet<>(MySharedPref.getSetSharedPref(MainActivity.this,
                                        MySharedPref.SET_OF_SHIFT, new HashSet<String>()));
                                if (!sendToTom.isEmpty()) {
                                    Uri uri = Uri.parse(DailyTodoContentProvider.CONTENT_URI + "/" + daily.getId());
                                    Iterator<String> i = sendToTom.iterator();
                                    d = i.next();
                                    goal = d.split(",");
                                    daily.setTittle(goal[0]);
                                    daily.setDescription(goal[1]);
                                    getContentResolver().update(uri, daily.toContentValues(), null, null);
                                    sendToTom.remove(d);
                                }
                            }
                            MySharedPref.putSetSharedPref(MainActivity.this, MySharedPref.SET_OF_SHIFT, sendToTom);
                        } while (data.moveToNext());
                    }
                    data.close();
                }
                break;
            case LOADER_ARCHIVE:
                if (data != null) {
                    //Log.d(TAG, "We have " + data.getCount() + " items in archive");
                    if (data.moveToFirst()) {
                        List<TodoDaily> items = new ArrayList<>();
                        do {
                            items.add(TodoDaily.buildFromCursor(data));
                        } while (data.moveToNext());

                        archiveAdepter.swapToday(items);
                    }
                }
                break;
            case LOADER_HELP:
                if (data != null) {
                    //Log.d(TAG, "We have " + data.getCount() + " items in help");
                    if (data.moveToFirst()) {
                        List<TodoDaily> items = new ArrayList<>();
                        do {
                            items.add(TodoDaily.buildFromCursor(data));
                        } while (data.moveToNext());

                        helpAdepter.swapToday(items);
                    } else
                        helpAdepter.swapToday(new ArrayList<TodoDaily>());
                }
                break;
            case LOADER_NOT_DONE:
                if (data != null) {
                    //Log.d(TAG, "We have " + data.getCount() + " items in not done");
                    if (data.moveToFirst()) {
                        List<TodoDaily> items = new ArrayList<>();
                        do {
                            items.add(TodoDaily.buildFromCursor(data));
                        } while (data.moveToNext());
                        notDoneAdepter.swapToday(items);
                    } else {
                        notDoneAdepter.swapToday(new ArrayList<TodoDaily>());
                    }
                    MySharedPref.putIntSharedPref(this, MySharedPref.CHECK_IF_NOT_DONE_EMPTY, notDoneAdepter.getItemCount());
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //Log.d(TAG, "Loader reset");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOADER_TODAY);
        getSupportLoaderManager().destroyLoader(LOADER_ARCHIVE);
        getSupportLoaderManager().destroyLoader(LOADER_HELP);
        getSupportLoaderManager().destroyLoader(LOADER_NOT_DONE);
        getSupportLoaderManager().destroyLoader(LOADER_TOM);
    }

    public class MyPagerAdepter extends PagerAdapter {
        @Override
        public int getCount() {
            tabCount = 4;
            return 4;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof View)
                container.removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View pagerView = inflater.inflate(R.layout.recycler_for_layouts, null);
            RecyclerView recyclerView = null;
            switch (position) {
                case 0:
                    recyclerView = (RecyclerView) pagerView.findViewById(R.id.recycler_view_today);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(todayAdepter);
                    break;
                case 1:
                    recyclerView = (RecyclerView) pagerView.findViewById(R.id.recycler_view_archive);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(archiveAdepter);
                    break;
                case 2:
                    recyclerView = (RecyclerView) pagerView.findViewById(R.id.recycler_view_help);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(helpAdepter);
                    break;
                case 3:
                    recyclerView = (RecyclerView) pagerView.findViewById(R.id.recycler_view_not_done);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(notDoneAdepter);
                    break;

            }
            container.addView(recyclerView);
            return recyclerView;
        }
    }
}


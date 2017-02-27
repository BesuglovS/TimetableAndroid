package ru.besuglovs.nu.timetable;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.apiInterfaces.ScheduleInterface;
import ru.besuglovs.nu.timetable.fragments.LoadWholeScheduleFragment;
import ru.besuglovs.nu.timetable.fragments.TabsFragment;
import ru.besuglovs.nu.timetable.fragments.TabsNoConnectionFragment;
import ru.besuglovs.nu.timetable.fragments.groupListFragment;
import ru.besuglovs.nu.timetable.timetable.DBHelper;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;
import ru.besuglovs.nu.timetable.timetable.Timetable;


public class MainActivity extends AppCompatActivity
    implements groupListFragment.Callbacks,
        TabsFragment.SwitchNoNetwork,
        TabsNoConnectionFragment.SwitchToNetwork
{

    public static final String API_ENDPOINT = "http://wiki.nayanova.edu/";
    //public static final String FILENAME = "timetable.json";

    public static boolean NETWORK = false;
    //public static boolean SAVED_SCHEDULE = false;
    private static boolean DB_EXISTS = false;

    public static ScheduleInterface api;
    public static Integer groupId = -1;
    public static String groupName = "";
    private static List<StudentGroup> groupList;
    public static List<StudentGroup> mainGroups;

    private DBHelper dbHelper = null;

    public DBHelper getHelper() {
        if (dbHelper == null) {
            dbHelper =
                    OpenHelperManager.getHelper(this, DBHelper.class);
        }
        return dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get network status
        NETWORK = isConnected();

        //SAVED_SCHEDULE = fileExistence(FILENAME);
        DB_EXISTS = dbExists();

        if (NETWORK)
        {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.API_ENDPOINT)
                    .build();

            api = adapter.create(ScheduleInterface.class);

            api.getStudentGroupList(new Callback<List<StudentGroup>>() {
                @Override
                public void success(List<StudentGroup> studentGroups, Response response) {
                    groupList = studentGroups;

                    mainGroups = new ArrayList<>();
                    for(StudentGroup group : groupList)
                    {
                        if (!group.Name.contains("+") &&
                                !group.Name.contains("-") &&
                                !group.Name.contains("|") &&
                                !group.Name.contains("I"))
                        {
                            mainGroups.add(group);
                        }
                    }

                    Collections.sort(mainGroups, new StudentGroupNameComparator());

                    if (mainGroups != null & mainGroups.size() > 0) {
                        MainActivity.groupId = mainGroups.get(0).StudentGroupId;
                        MainActivity.groupName = mainGroups.get(0).Name;

                        setTitle(groupName);
                    }

                    TabsFragment fragment = new TabsFragment();
                    getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, fragment)
                            .commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // TODO Grace error handling
                }
            });



            Log.d("Timetable", "Start loading");
            api.getWholeSchedule(new Callback<Timetable>() {
                @Override
                public void success(Timetable result, Response response) {
                    Log.d("Timetable", "Finish loading");
                    try {
                        LoadWholeScheduleFragment loadingFragment = new LoadWholeScheduleFragment();
                        getFragmentManager()
                                .beginTransaction()
                                .add(loadingFragment, "loadingFragment")
                                .commit();
                        loadingFragment.StartTask(result);
                    } catch (Exception e) {
                        // TODO Grace error handling
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // TODO Grace error handling
                }
            });
        }
        else
        // NO NETWORK
        {
            if (DB_EXISTS)
            // File with schedule exists
            {
                List<StudentGroup> studentGroups = null;
                try {
                    studentGroups = getHelper().getStudentGroupDao().queryForAll();
                } catch (SQLException e) {
                    // TODO Grace error handling
                }

                mainGroups = new ArrayList<>();
                if (studentGroups != null) {
                    for (StudentGroup group : studentGroups) {
                        if (!group.Name.contains("+") &&
                                !group.Name.contains("-") &&
                                !group.Name.contains("|") &&
                                !group.Name.contains("I")) {
                            mainGroups.add(group);
                        }
                    }


                    Collections.sort(mainGroups, new StudentGroupNameComparator());

                    if (studentGroups.size() > 0) {
                        groupId = studentGroups.get(0).StudentGroupId;
                        groupName = studentGroups.get(0).Name;

                        setTitle(groupName);
                    }
                }

                TabsNoConnectionFragment fragment = new TabsNoConnectionFragment();
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commit();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Нет ни интернета, ни сохранённого расписания.", Toast.LENGTH_LONG).show();
            }

        }
    }

    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @SuppressWarnings("unused")
    public boolean fileExistence(String filename){
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }

    private boolean dbExists() {
        File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
        return dbFile.exists();
    }

    @Override
    public void DoNoNetwork() {
        TabsNoConnectionFragment fragment = new TabsNoConnectionFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    @Override
    public void DoToNetwork() {
        TabsFragment fragment = new TabsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    private static class StudentGroupNameComparator implements Comparator<StudentGroup> {
        @Override
        public int compare(StudentGroup group1, StudentGroup group2) {
            return group1.Name.compareTo(group2.Name);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_changeGroup:
                if (mainGroups != null) {
                    groupListFragment listFragment = new groupListFragment();
                    listFragment.show(getFragmentManager(), "chooseGroup");
                }
                else {
                    Toast.makeText(this, R.string.mainGroupsNotLoadedYet, Toast.LENGTH_LONG);
                }
                return true;
            case R.id.action_teachersSchedule:
                Intent teachersScheduleIntent = new Intent(this, TeacherActivity.class);
                startActivity(teachersScheduleIntent);
                return true;
            case R.id.action_session:
                Intent sessionIntent = new Intent(this, Session.class);
                startActivity(sessionIntent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGroupSelected(StudentGroup group) {
        groupId = group.StudentGroupId;
        groupName = group.Name;

        setTitle(groupName);

        NETWORK = isConnected();

        if (NETWORK) {
            TabsFragment fragment = new TabsFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
        else
        {
            TabsNoConnectionFragment fragment = new TabsNoConnectionFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
    }
}

package ru.besuglovs.nu.timetable;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.apiInterfaces.ScheduleInterface;
import ru.besuglovs.nu.timetable.fragments.SessionScheduleFragment;
import ru.besuglovs.nu.timetable.fragments.TabsFragment;
import ru.besuglovs.nu.timetable.fragments.TeacherScheduleTabsFragment;
import ru.besuglovs.nu.timetable.fragments.groupListFragment;
import ru.besuglovs.nu.timetable.fragments.sessionGroupListFragment;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;
import ru.besuglovs.nu.timetable.timetable.Teacher;


public class Session extends ActionBarActivity
    implements sessionGroupListFragment.Callbacks
{

    public static ScheduleInterface api;
    public static Integer groupId = -1;
    public static String groupName = "";
    public static List<StudentGroup> groupList;
    public static List<StudentGroup> mainGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        // Get network status
        MainActivity.NETWORK = isConnected();

        if (MainActivity.NETWORK)
        {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.API_ENDPOINT)
                    .build();

            api = adapter.create(ScheduleInterface.class);

            api.getExamStudentGroupList(new Callback<List<StudentGroup>>() {
                @Override
                public void success(List<StudentGroup> studentGroups, Response response) {
                    groupList = studentGroups;

                    mainGroups = new ArrayList<StudentGroup>();
                    for (StudentGroup group : groupList) {
                        if (!group.Name.contains("+") &&
                                !group.Name.contains("-") &&
                                !group.Name.contains("|") &&
                                !group.Name.contains("I")) {
                            mainGroups.add(group);
                        }
                    }

                    Collections.sort(mainGroups, new StudentGroupNameComparator());

                    if (mainGroups != null & mainGroups.size() > 0) {
                        Session.groupId = mainGroups.get(0).StudentGroupId;
                        Session.groupName = mainGroups.get(0).Name;

                        setTitle(groupName);
                    }

                    SessionScheduleFragment fragment = new SessionScheduleFragment();
                    getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, fragment)
                            .commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(Session.this, retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        // NO NETWORK
        {
            Toast.makeText(Session.this, "Нет интернета.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onGroupSelected(StudentGroup group) {
        groupId = group.StudentGroupId;
        groupName = group.Name;

        setTitle(groupName);

        if (MainActivity.NETWORK) {
            SessionScheduleFragment fragment = new SessionScheduleFragment();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
    }

    public static class StudentGroupNameComparator implements Comparator<StudentGroup> {
        @Override
        public int compare(StudentGroup group1, StudentGroup group2) {
            return group1.Name.compareTo(group2.Name);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_changeSessionGroup:
                sessionGroupListFragment listFragment = new sessionGroupListFragment();
                listFragment.show(getFragmentManager(), "chooseGroup");
                return true;
            case R.id.action_lessonsSchedule:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_teachersSchedule:
                Intent teacherIntent = new Intent(this, TeacherActivity.class);
                startActivity(teacherIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

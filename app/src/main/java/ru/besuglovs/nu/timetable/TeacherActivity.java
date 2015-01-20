package ru.besuglovs.nu.timetable;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.apiInterfaces.ScheduleInterface;
import ru.besuglovs.nu.timetable.fragments.TeacherScheduleTabsFragment;
import ru.besuglovs.nu.timetable.fragments.teacherListFragment;
import ru.besuglovs.nu.timetable.timetable.Teacher;


public class TeacherActivity extends ActionBarActivity
    implements teacherListFragment.TeacherChoiceCallbacks
{
    public static ScheduleInterface api;
    public static Integer teacherId = -1;
    public static String teacherName = "";
    public static List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        // Get network status
        MainActivity.NETWORK = isConnected();

        if (MainActivity.NETWORK)
        {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.API_ENDPOINT)
                    .build();

            api = adapter.create(ScheduleInterface.class);

            api.getTeachersList(new Callback<List<Teacher>>() {
                @Override
                public void success(List<Teacher> teachers, Response response) {

                    Collections.sort(teachers, new TeacherNameComparator());

                    teacherList = teachers;

                    if (teacherList != null & teacherList.size() > 0) {
                        TeacherActivity.teacherId = teacherList.get(0).TeacherId;
                        TeacherActivity.teacherName = teacherList.get(0).FIO;

                        setTitle(teacherName);
                    }

                    TeacherScheduleTabsFragment fragment = new TeacherScheduleTabsFragment();
                    getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, fragment)
                            .commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Toast.makeText(TeacherActivity.this, retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        // NO NETWORK
        {
            Toast.makeText(TeacherActivity.this, "Нет интернета.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTeacherSelected(Teacher teacher) {
        teacherId = teacher.TeacherId;
        teacherName = teacher.FIO;

        setTitle(teacherName);

        TeacherScheduleTabsFragment fragment = new TeacherScheduleTabsFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();
    }

    public static class TeacherNameComparator implements Comparator<Teacher> {
        @Override
        public int compare(Teacher teacher1, Teacher teacher2) {
            return teacher1.FIO.compareTo(teacher2.FIO);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teacher_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_changeTeacher:
                teacherListFragment listFragment = new teacherListFragment();
                listFragment.show(getFragmentManager(), "chooseTeacher");
                return true;
            case R.id.action_lessonsSchedule:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_session:
                Intent sessionIntent = new Intent(this, Session.class);
                startActivity(sessionIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package ru.besuglovs.nu.timetable.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.Components.NestedListView;
import ru.besuglovs.nu.timetable.ListAdapters.GroupExamsAdapter;
import ru.besuglovs.nu.timetable.ListAdapters.WeekScheduleAdapter;
import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.Session;
import ru.besuglovs.nu.timetable.apiViews.GroupExam;
import ru.besuglovs.nu.timetable.apiViews.GroupExams;

/**
 * Created by bs on 19.01.2015.
 */
public class SessionScheduleFragment extends Fragment {

    Session activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Session activity = (Session) getActivity();

        MainActivity.NETWORK =  activity.isConnected();

        // Inflate a new layout from our resources
        final View view = inflater.inflate(R.layout.fragment_session,
                container, false);

        Session.api.getGroupExams(Session.groupId, new Callback<Map<Integer, GroupExams>>() {
            @Override
            public void success(Map<Integer, GroupExams> groupExams, Response response) {
                showGroupExams(view, groupExams);
            }

            @Override
            public void failure(RetrofitError error) {
                showGroupExams(view, "Epic fail: " + error.getMessage());
            }
        });

        // Return the View
        return view;
    }

    public void showGroupExams(View view, String text)
    {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        Log.e("bigError", text);
    }

    public void showGroupExams(View view, Map<Integer, GroupExams> groupExams)
    {
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatOut = new SimpleDateFormat("dd.MM.yyyy");

        List<GroupExam> exams = groupExams.get(groupExams.keySet().toArray()[0]).Exams;

        ListView lv = (ListView) view.findViewById(R.id.sessionListView);
        GroupExamsAdapter groupExamsAdapter = new GroupExamsAdapter(exams, getActivity().getLayoutInflater());
        lv.setAdapter(groupExamsAdapter);
    }
}

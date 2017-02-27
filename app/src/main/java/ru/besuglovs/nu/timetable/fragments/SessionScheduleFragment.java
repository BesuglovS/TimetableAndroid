package ru.besuglovs.nu.timetable.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.ListAdapters.GroupExamsAdapter;
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
                //TODO Grace error handling
            }
        });

        // Return the View
        return view;
    }

    private void showGroupExams(View view, Map<Integer, GroupExams> groupExams)
    {
        List<GroupExam> exams = groupExams.get(groupExams.keySet().toArray()[0]).Exams;

        ListView lv = (ListView) view.findViewById(R.id.sessionListView);
        GroupExamsAdapter groupExamsAdapter = new GroupExamsAdapter(exams, getActivity().getLayoutInflater());
        lv.setAdapter(groupExamsAdapter);
    }
}

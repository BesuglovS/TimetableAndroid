package ru.besuglovs.nu.timetable.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.besuglovs.nu.timetable.ListAdapters.TeacherAdapter;
import ru.besuglovs.nu.timetable.TeacherActivity;
import ru.besuglovs.nu.timetable.timetable.Teacher;

/**
 * Created by bs on 19.01.2015.
 */
public class teacherListFragment extends DialogFragment {

    private TeacherChoiceCallbacks activity;

    public teacherListFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TeacherAdapter adapter = new TeacherAdapter(TeacherActivity.teacherList, getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите преподавателя")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.onTeacherSelected(TeacherActivity.teacherList.get(which));
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface TeacherChoiceCallbacks {
        public void onTeacherSelected(Teacher teacher);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TeacherChoiceCallbacks) activity;
    }
}

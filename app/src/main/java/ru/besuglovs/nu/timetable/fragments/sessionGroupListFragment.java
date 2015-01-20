package ru.besuglovs.nu.timetable.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.besuglovs.nu.timetable.ListAdapters.StudentGroupAdapter;
import ru.besuglovs.nu.timetable.Session;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;

/**
 * Created by bs on 19.01.2015.
 */
public class sessionGroupListFragment extends DialogFragment {

    private Callbacks activity;

    public sessionGroupListFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        StudentGroupAdapter adapter = new StudentGroupAdapter(Session.mainGroups, getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите группу")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.onGroupSelected(Session.mainGroups.get(which));
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface Callbacks {
        public void onGroupSelected(StudentGroup group);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Callbacks) activity;
    }
}

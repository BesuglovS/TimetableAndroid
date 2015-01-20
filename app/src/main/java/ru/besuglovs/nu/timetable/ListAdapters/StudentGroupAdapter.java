package ru.besuglovs.nu.timetable.ListAdapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.timetable.StudentGroup;


/**
 * Created by Sorry_000 on 01.01.15.
 */
public class StudentGroupAdapter extends BaseAdapter {

    private final Context context;
    private final List<StudentGroup> groups;

    public StudentGroupAdapter(List<StudentGroup> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).StudentGroupId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(groups.get(position).Name);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}

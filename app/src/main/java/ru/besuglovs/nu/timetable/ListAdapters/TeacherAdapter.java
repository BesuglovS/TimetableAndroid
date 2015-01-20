package ru.besuglovs.nu.timetable.ListAdapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.timetable.Teacher;

/**
 * Created by bs on 19.01.2015.
 */
public class TeacherAdapter extends BaseAdapter {

    private final Context context;
    private final List<Teacher> teachers;

    public TeacherAdapter(List<Teacher> teachers, Context context) {
        this.teachers = teachers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public Object getItem(int position) {
        return teachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return teachers.get(position).TeacherId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);
        tv.setText(teachers.get(position).FIO);
        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}

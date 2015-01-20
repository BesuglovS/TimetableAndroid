package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.teacherWeekLesson;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;

/**
 * Created by Sorry_000 on 18.01.15.
 */
public class TeacherWeekScheduleAdapter extends BaseAdapter {
    List<teacherWeekLesson> lessons;
    LayoutInflater inflater;

    public TeacherWeekScheduleAdapter(List<teacherWeekLesson> lessons, LayoutInflater inflater) {
        this.lessons = lessons;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.teacher_week_lesson, parent, false);

        teacherWeekLesson l = lessons.get(position);

        TextView groupname = (TextView) view.findViewById(R.id.groupName);
        groupname.setText(l.groupName);

        TextView discName = (TextView) view.findViewById(R.id.discName);
        discName.setText(l.disciplineName);

        TextView audName = (TextView) view.findViewById(R.id.audName);
        audName.setText(l.auditoriumName);

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setText(l.Time.substring(0,5));

        return view;
    }
}

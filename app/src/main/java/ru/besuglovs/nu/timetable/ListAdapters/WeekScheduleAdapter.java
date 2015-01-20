package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;

/**
 * Created by bs on 30.12.2014.
 */
public class WeekScheduleAdapter extends BaseAdapter {

    List<weekLesson> lessons;
    LayoutInflater inflater;

    public WeekScheduleAdapter(List<weekLesson> lessons, LayoutInflater inflater) {
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
        View view = inflater.inflate(R.layout.week_lesson, parent, false);

        weekLesson l = lessons.get(position);

        String disciplineName = l.discName;

        if (!l.groupName.equals(MainActivity.groupName))
        {
            disciplineName += " (" + l.groupName + ")";
        }

        TextView discName = (TextView) view.findViewById(R.id.discName);
        discName.setText(disciplineName);

        TextView teacherFIO = (TextView) view.findViewById(R.id.teacherFIO);
        teacherFIO.setText(l.FIO);

        TextView audName = (TextView) view.findViewById(R.id.audName);
        audName.setText(l.audName);

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setText(l.Time.substring(0,5));

        return view;
    }
}

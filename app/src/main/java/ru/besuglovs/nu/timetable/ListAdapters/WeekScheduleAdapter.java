package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.ListAdapters.ViewHolders.WeekScheduleViewHolder;
import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;

/**
 * Created by bs on 30.12.2014.
 */
public class WeekScheduleAdapter extends BaseAdapter {

    private final List<weekLesson> lessons;
    private final LayoutInflater inflater;

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
        View vi = convertView;
        WeekScheduleViewHolder weekScheduleViewHolder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.week_lesson, null);

            weekScheduleViewHolder = new WeekScheduleViewHolder();
            weekScheduleViewHolder.lesson_audName  = (TextView) vi.findViewById(R.id.audName);
            weekScheduleViewHolder.lesson_discName  = (TextView) vi.findViewById(R.id.discName);
            weekScheduleViewHolder.lesson_teacherFIO = (TextView) vi.findViewById(R.id.teacherFIO);
            weekScheduleViewHolder.lesson_time = (TextView) vi.findViewById(R.id.time);

            vi.setTag(weekScheduleViewHolder);

        } else
            weekScheduleViewHolder = (WeekScheduleViewHolder) vi.getTag();

        weekLesson l = lessons.get(position);

        String disciplineName = l.discName;

        if (!l.groupName.equals(MainActivity.groupName))
        {
            disciplineName += " (" + l.groupName + ")";
        }

        // now set your text view here like
        weekScheduleViewHolder.lesson_audName.setText(l.audName);
        weekScheduleViewHolder.lesson_teacherFIO.setText(l.FIO);
        weekScheduleViewHolder.lesson_discName.setText(disciplineName);
        weekScheduleViewHolder.lesson_time.setText(l.Time.substring(0,5));


        // return your view
        return vi;
    }
}

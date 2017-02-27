package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.ListAdapters.ViewHolders.TeacherWeekScheduleViewHolder;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.teacherWeekLesson;

/**
 * Created by Sorry_000 on 18.01.15.
 */
public class TeacherWeekScheduleAdapter extends BaseAdapter {
    private final List<teacherWeekLesson> lessons;
    private final LayoutInflater inflater;

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
        View vi = convertView;
        TeacherWeekScheduleViewHolder teacherWeekScheduleViewHolder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.teacher_week_lesson, null);

            teacherWeekScheduleViewHolder = new TeacherWeekScheduleViewHolder();
            teacherWeekScheduleViewHolder.groupname  = (TextView) vi.findViewById(R.id.groupName);
            teacherWeekScheduleViewHolder.discName = (TextView) vi.findViewById(R.id.discName);
            teacherWeekScheduleViewHolder.audName = (TextView) vi.findViewById(R.id.audName);
            teacherWeekScheduleViewHolder.time = (TextView) vi.findViewById(R.id.time);

            vi.setTag(teacherWeekScheduleViewHolder);

        } else {
            teacherWeekScheduleViewHolder = (TeacherWeekScheduleViewHolder) vi.getTag();
        }

        teacherWeekLesson l = lessons.get(position);

        // now set your text view here like
        teacherWeekScheduleViewHolder.groupname.setText(l.groupName);
        teacherWeekScheduleViewHolder.discName.setText(l.disciplineName);
        teacherWeekScheduleViewHolder.audName.setText(l.auditoriumName);
        teacherWeekScheduleViewHolder.time.setText(l.Time);

        // return your view
        return vi;
    }
}

package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.GroupExam;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;

/**
 * Created by bs on 19.01.2015.
 */
public class GroupExamsAdapter extends BaseAdapter {

    List<GroupExam> exams;
    LayoutInflater inflater;

    public GroupExamsAdapter(List<GroupExam> exams, LayoutInflater inflater) {
        this.exams = exams;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return exams.size();
    }

    @Override
    public Object getItem(int position) {
        return exams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.session_exam, parent, false);

        GroupExam e = exams.get(position);

        TextView discName = (TextView) view.findViewById(R.id.discName);
        discName.setText(e.DisciplineName);

        TextView teacherFIO = (TextView) view.findViewById(R.id.teacherFIO);
        teacherFIO.setText(e.TeacherFIO);

        TextView consDatetime = (TextView) view.findViewById(R.id.consDateTime);
        consDatetime.setText(e.ConsultationDateTime);

        TextView consAud = (TextView) view.findViewById(R.id.consAud);
        consAud.setText(e.ConsultationAuditoriumName);

        TextView examDatetime= (TextView) view.findViewById(R.id.examDateTime);
        examDatetime.setText(e.ExamDateTime);

        TextView examAud = (TextView) view.findViewById(R.id.examAud);
        examAud.setText(e.ExamAuditoriumName);

        return view;
    }
}

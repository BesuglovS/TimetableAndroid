package ru.besuglovs.nu.timetable.ListAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.besuglovs.nu.timetable.ListAdapters.ViewHolders.GroupExamsViewHolder;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.GroupExam;

/**
 * Created by bs on 19.01.2015.
 */
public class GroupExamsAdapter extends BaseAdapter {

    private final List<GroupExam> exams;
    private final LayoutInflater inflater;

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
        View vi = convertView;
        GroupExamsViewHolder groupExamsViewHolder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.session_exam, null);

            groupExamsViewHolder = new GroupExamsViewHolder();
            groupExamsViewHolder.discName  = (TextView) vi.findViewById(R.id.discName);
            groupExamsViewHolder.teacherFIO = (TextView) vi.findViewById(R.id.teacherFIO);
            groupExamsViewHolder.consDatetime = (TextView) vi.findViewById(R.id.consDateTime);
            groupExamsViewHolder.consAud = (TextView) vi.findViewById(R.id.consAud);
            groupExamsViewHolder.examDatetime = (TextView) vi.findViewById(R.id.examDateTime);
            groupExamsViewHolder.examAud = (TextView) vi.findViewById(R.id.examAud);

            vi.setTag(groupExamsViewHolder);

        } else {
            groupExamsViewHolder = (GroupExamsViewHolder) vi.getTag();
        }

        GroupExam e = exams.get(position);

        // now set your text view here like
        groupExamsViewHolder.discName.setText(e.DisciplineName);
        groupExamsViewHolder.teacherFIO.setText(e.TeacherFIO);
        groupExamsViewHolder.consDatetime.setText(e.ConsultationDateTime);
        groupExamsViewHolder.consAud.setText(e.ConsultationAuditoriumName);
        groupExamsViewHolder.examDatetime.setText(e.ExamDateTime);
        groupExamsViewHolder.examAud.setText(e.ExamAuditoriumName);


        // return your view
        return vi;
    }
}

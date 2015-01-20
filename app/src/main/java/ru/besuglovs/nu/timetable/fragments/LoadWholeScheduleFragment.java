package ru.besuglovs.nu.timetable.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.timetable.Auditorium;
import ru.besuglovs.nu.timetable.timetable.Calendar;
import ru.besuglovs.nu.timetable.timetable.ConfigOption;
import ru.besuglovs.nu.timetable.timetable.Discipline;
import ru.besuglovs.nu.timetable.timetable.Lesson;
import ru.besuglovs.nu.timetable.timetable.LessonLogEvent;
import ru.besuglovs.nu.timetable.timetable.Ring;
import ru.besuglovs.nu.timetable.timetable.Student;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;
import ru.besuglovs.nu.timetable.timetable.StudentsInGroups;
import ru.besuglovs.nu.timetable.timetable.Teacher;
import ru.besuglovs.nu.timetable.timetable.TeacherForDiscipline;
import ru.besuglovs.nu.timetable.timetable.Timetable;

/**
 * Created by Sorry_000 on 03.01.15.
 */
public class LoadWholeScheduleFragment extends Fragment {

    private AsyncTask<Timetable, Void, Void> loadingTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (loadingTask != null)
        {
            loadingTask.cancel(true);
        }
    }

    public void StartTask(Timetable timetable)
    {
        loadingTask = new LoadingTask();
        loadingTask.execute(timetable);
    }

    private class LoadingTask extends AsyncTask<Timetable, Void, Void> {

        @Override
        protected Void doInBackground(Timetable... params) {

            Timetable result = params[0];

            MainActivity activity = (MainActivity) getActivity();

            try {
                activity.getHelper().ClearDB();

                Log.d("Timetable", "Writing starting...");
                Dao<Auditorium, Integer> AudDAO = activity.getHelper().getAuditoriumDao();
                for (Auditorium aud : result.auditoriums) {
                    AudDAO.create(aud);
                }

                Dao<Calendar, Integer> CalendarDAO = activity.getHelper().getCalendarDao();
                for (Calendar cal : result.calendars) {
                    CalendarDAO.create(cal);
                }

                Dao<Discipline, Integer> DisciplinesDAO = activity.getHelper().getDisciplineDao();
                for (Discipline discipline : result.disciplines) {
                    DisciplinesDAO.create(discipline);
                }

                Dao<Lesson, Integer> LessonDAO = activity.getHelper().getLessonDao();
                for (Lesson lesson : result.lessons) {
                    LessonDAO.create(lesson);
                }

                Dao<Ring, Integer> RingDAO = activity.getHelper().getRingDao();
                for (Ring ring : result.rings) {
                    RingDAO.create(ring);
                }

                Dao<Student, Integer> StudentDAO = activity.getHelper().getStudentDao();
                for (Student student : result.students) {
                    StudentDAO.create(student);
                }

                Dao<StudentGroup, Integer> StudentGroupDAO = activity.getHelper().getStudentGroupDao();
                for (StudentGroup studentGroup : result.studentGroups) {
                    StudentGroupDAO.create(studentGroup);
                }

                Dao<StudentsInGroups, Integer> StudentsInGroupsDAO = activity.getHelper().getStudentsInGroupsDao();
                for (StudentsInGroups studentsInGroups : result.studentsInGroups) {
                    StudentsInGroupsDAO.create(studentsInGroups);
                }

                Dao<Teacher, Integer> TeacherDAO = activity.getHelper().getTeacherDao();
                for (Teacher teacher : result.teachers) {
                    TeacherDAO.create(teacher);
                }

                Dao<TeacherForDiscipline, Integer> TeacherForDisciplineDAO =
                        activity.getHelper().getTeacherForDisciplineDao();
                for (TeacherForDiscipline teacherForDiscipline : result.teacherForDisciplines) {
                    TeacherForDisciplineDAO.create(teacherForDiscipline);
                }

                Dao<ConfigOption, Integer> ConfigOptionDAO = activity.getHelper().getConfigOptionDao();
                for (ConfigOption configOption : result.configOptions) {
                    ConfigOptionDAO.create(configOption);
                }

                Dao<LessonLogEvent, Integer> LessonLogEventDAO = activity.getHelper().getLessonLogEventDao();
                for (LessonLogEvent lessonLogEvent : result.lessonLogEvents) {
                    LessonLogEventDAO.create(lessonLogEvent);
                }

                Log.d("Timetable", "Writing done");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

package ru.besuglovs.nu.timetable.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.besuglovs.nu.timetable.Components.NestedListView;
import ru.besuglovs.nu.timetable.ListAdapters.WeekScheduleAdapter;
import ru.besuglovs.nu.timetable.MainActivity;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;
import ru.besuglovs.nu.timetable.timetable.Auditorium;
import ru.besuglovs.nu.timetable.timetable.ConfigOption;
import ru.besuglovs.nu.timetable.timetable.DBHelper;
import ru.besuglovs.nu.timetable.timetable.Discipline;
import ru.besuglovs.nu.timetable.timetable.Lesson;
import ru.besuglovs.nu.timetable.timetable.Ring;
import ru.besuglovs.nu.timetable.timetable.Student;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;
import ru.besuglovs.nu.timetable.timetable.StudentsInGroups;
import ru.besuglovs.nu.timetable.timetable.Teacher;
import ru.besuglovs.nu.timetable.timetable.TeacherForDiscipline;
import ru.besuglovs.nu.timetable.timetable.Timetable;
import ru.besuglovs.nu.timetable.view.SlidingTabLayout;


public class TabsNoConnectionFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private Timetable timetable;

    public TabsNoConnectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 18;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return "Неделя " + (position + 1);
            return Integer.toString(position+1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            MainActivity activity = (MainActivity) getActivity();
            MainActivity.NETWORK =  activity.isConnected();
            if (MainActivity.NETWORK)
            {
                activity.DoToNetwork();
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.week_item,
                        container, false);
                // Add the newly created View to the ViewPager
                container.addView(view);

                return view;
            }

            // Inflate a new layout from our resources
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.week_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            int week = position + 1;

            List<weekLesson> weekLessons = null;
            try {
                weekLessons = GetWeekLessonsFromDB(MainActivity.groupId, week);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            showWeekSchedule(view, weekLessons);

            // Return the View
            return view;
        }

        private List<weekLesson> GetWeekLessonsFromDB(Integer groupId, int week) throws SQLException, ParseException {
            MainActivity activity = (MainActivity) getActivity();
            DBHelper dbHelper = activity.getHelper();

            // SemesterStarts
            List<ConfigOption> queryResult = dbHelper.getConfigOptionDao().queryForEq("Key", "Semester Starts");
            String SemesterStartsString = queryResult.get(0).Value;
            SimpleDateFormat mySqlFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date SemesterStarts = mySqlFormat.parse(SemesterStartsString);
            Calendar ssCalendar = new GregorianCalendar();
            ssCalendar.setTime(SemesterStarts);

            ssCalendar.add(Calendar.DATE, 7*(week-1));
            ArrayList<String> weekDates = new ArrayList<>();
            String weekDate;
            for(int i = 0; i < 7; i++)
            {
                weekDate = mySqlFormat.format(ssCalendar.getTime());
                weekDates.add(weekDate);
                ssCalendar.add(Calendar.DATE, 1);
            }

            QueryBuilder<ru.besuglovs.nu.timetable.timetable.Calendar, Integer> qb =
                    dbHelper.getCalendarDao().queryBuilder();
            qb.where().in("Date", weekDates);
            PreparedQuery<ru.besuglovs.nu.timetable.timetable.Calendar> preparedQuery = qb.prepare();
            List<ru.besuglovs.nu.timetable.timetable.Calendar> calendars =
                    dbHelper.getCalendarDao().query(preparedQuery);
            List<Integer> calendarIds = new ArrayList<>();
            Map<Integer, String> calendarWithIds = new HashMap<>();
            for (ru.besuglovs.nu.timetable.timetable.Calendar calendar : calendars)
            {
                calendarIds.add(calendar.CalendarId);
                calendarWithIds.put(calendar.CalendarId, calendar.Date);
            }

            QueryBuilder<Lesson, Integer> qbL =
                    dbHelper.getLessonDao().queryBuilder();
            qbL.where().in("CalendarId", calendarIds);
            PreparedQuery<Lesson> preparedQueryL = qbL.prepare();
            List<Lesson> lessonList = dbHelper.getLessonDao().query(preparedQueryL);

            List<Ring> rings = dbHelper.getRingDao().queryForAll();
            Map<Integer, String> ringWithIds = new HashMap<>();
            for(Ring ring : rings)
            {
                ringWithIds.put(ring.RingId, ring.Time);
            }

            List<Discipline> discs = dbHelper.getDisciplineDao().queryForAll();
            Map<Integer, Discipline> discsWithIds = new HashMap<>();
            for (Discipline disc : discs)
            {
                discsWithIds.put(disc.DisciplineId, disc);
            }

            List<TeacherForDiscipline> tfdList = dbHelper.getTeacherForDisciplineDao().queryForAll();
            Map<Integer, TeacherForDiscipline> tfdWithIds = new HashMap<>();
            for(TeacherForDiscipline tfd : tfdList)
            {
                tfdWithIds.put(tfd.TeacherForDisciplineId, tfd);
            }

            List<Teacher> teachers = dbHelper.getTeacherDao().queryForAll();
            Map<Integer, Teacher> teacherWithIds = new HashMap<>();
            for(Teacher teacher : teachers)
            {
                teacherWithIds.put(teacher.TeacherId, teacher);
            }

            List<Auditorium> auds = dbHelper.getAuditoriumDao().queryForAll();
            Map<Integer, Auditorium> audWithIds = new HashMap<>();
            for (Auditorium aud : auds)
            {
                audWithIds.put(aud.AuditoriumId, aud);
            }

            List<StudentGroup> studentGroups = dbHelper.getStudentGroupDao().queryForAll();
            Map<Integer, StudentGroup> groupWithIds = new HashMap<>();
            for (StudentGroup g : studentGroups)
            {
                groupWithIds.put(g.StudentGroupId, g);
            }

            List<Student> students = dbHelper.getStudentDao().queryForAll();
            Map<Integer, Student> studWithIds = new HashMap<>();
            for (Student s : students)
            {
                studWithIds.put(s.StudentId, s);
            }

            List<StudentsInGroups> sigs = dbHelper.getStudentsInGroupsDao().queryForAll();
            Map<Integer, StudentsInGroups> sigWithIds = new HashMap<>();
            for (StudentsInGroups sig : sigs)
            {
                sigWithIds.put(sig.StudentsInGroupsId, sig);
            }

            //groupIds
            List<Integer> groupStudentIds = new ArrayList<>();
            for (StudentsInGroups sig : sigs)
            {
                if (sig.StudentGroupId.equals(groupId))
                {
                    if (!groupStudentIds.contains(sig.StudentGroupId)) {
                        groupStudentIds.add(sig.StudentId);
                    }
                }
            }
            List<Integer> groupIds = new ArrayList<>();
            for (StudentsInGroups sig : sigs)
            {
                if (groupStudentIds.contains(sig.StudentId))
                {
                    if (!groupIds.contains(sig.StudentGroupId)) {
                        groupIds.add(sig.StudentGroupId);
                    }
                }
            }

            List<Lesson> finalList = new ArrayList<>();
            for (Lesson lesson : lessonList)
            {
                if (lesson.IsActive.equals("0"))
                {
                    continue;
                }

                TeacherForDiscipline tfd = tfdWithIds.get(lesson.TeacherForDisciplineId);
                Discipline disc = discsWithIds.get(tfd.DisciplineId);

                if (!groupIds.contains(disc.StudentGroupId))
                {
                    continue;
                }

                finalList.add(lesson);
            }

            List<weekLesson> result = new ArrayList<>();

            for(Lesson lesson : finalList)
            {
                TeacherForDiscipline tfd = tfdWithIds.get(lesson.TeacherForDisciplineId);
                Discipline disc = discsWithIds.get(tfd.DisciplineId);

                weekLesson wl = new weekLesson();
                wl.date = calendarWithIds.get(lesson.CalendarId);
                wl.Time = ringWithIds.get(lesson.RingId);
                wl.discName = disc.Name;
                wl.FIO = teacherWithIds.get(tfd.TeacherId).FIO;
                wl.audName = audWithIds.get(lesson.AuditoriumId).Name;
                wl.groupName = groupWithIds.get(disc.StudentGroupId).Name;

                String dateString = calendarWithIds.get(lesson.CalendarId);
                Date d = mySqlFormat.parse(dateString);
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(d);
                Integer dow1 = calendar.get(Calendar.DAY_OF_WEEK);
                Integer dow2 = -1;
                switch (dow1)
                {
                    case Calendar.MONDAY:
                        dow2 = 1;
                        break;
                    case Calendar.TUESDAY:
                        dow2 = 2;
                        break;
                    case Calendar.WEDNESDAY:
                        dow2 = 3;
                        break;
                    case Calendar.THURSDAY:
                        dow2 = 4;
                        break;
                    case Calendar.FRIDAY:
                        dow2 = 5;
                        break;
                    case Calendar.SATURDAY:
                        dow2 = 6;
                        break;
                    case Calendar.SUNDAY:
                        dow2 = 7;
                        break;
                }

                wl.dow = dow2;

                result.add(wl);
            }

            return result;
        }

        public void showWeekSchedule(View view, String text)
        {
            Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        }

        public void showWeekSchedule(View view, List<weekLesson> weekLessons)
        {
            Map<Integer, String> dowNames = new HashMap<>();
            dowNames.put(1, "Понедельник");
            dowNames.put(2, "Вторник");
            dowNames.put(3, "Среда");
            dowNames.put(4, "Четверг");
            dowNames.put(5, "Пятница");
            dowNames.put(6, "Суббота");

            Map<Integer, List<weekLesson>> datedLessons = new HashMap<>();
            for(Integer i = 1; i < 7; i++)
            {
                datedLessons.put(i, new ArrayList<weekLesson>());
            }

            for (weekLesson wl : weekLessons)
            {
                datedLessons.get(wl.dow).add(wl);
            }

            for(Integer i = 1; i < 7; i++) {
                Collections.sort(datedLessons.get(i), new WeekLessonComparator());
            }

            ScrollView sv = (ScrollView) view.findViewById(R.id.scrollView);
            LinearLayout root = new LinearLayout(getActivity());
            root.setOrientation(LinearLayout.VERTICAL);
            sv.addView(root);

            SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatOut = new SimpleDateFormat("dd.MM.yyyy");

            Boolean emptyWeek = true;
            for(Integer dow = 1; dow < 7; dow++)
            {
                if (datedLessons.get(dow).size() > 0) {
                    emptyWeek = false;

                    String dateString = "";
                    try {
                        Date date = formatIn.parse(datedLessons.get(dow).get(0).date);
                        dateString = formatOut.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TextView dowName = new TextView(getActivity());
                    dowName.setText(dowNames.get(dow) + " (" + dateString + ")");
                    dowName.setBackgroundColor(getResources().getColor(R.color.somewhatBlueGreen));
                    dowName.setTextColor(getResources().getColor(R.color.lightGray));
                    dowName.setGravity(Gravity.CENTER);
                    root.addView(dowName);

                    NestedListView dowListView = new NestedListView(getActivity(), null);
                    WeekScheduleAdapter adapter =
                            new WeekScheduleAdapter(datedLessons.get(dow), getActivity().getLayoutInflater());
                    dowListView.setAdapter(adapter);
                    root.addView(dowListView);
                }
            }

            if (emptyWeek){
                TextView dowName = new TextView(getActivity());
                dowName.setText("Занятий нет");
                dowName.setBackgroundColor(getResources().getColor(R.color.somewhatBlueGreen));
                dowName.setTextColor(getResources().getColor(R.color.lightGray));
                dowName.setGravity(Gravity.CENTER);
                root.addView(dowName);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private class WeekLessonComparator implements java.util.Comparator<weekLesson> {

            @Override
            public int compare(weekLesson lesson1, weekLesson lesson2) {
                Integer l1h = Integer.parseInt(lesson1.Time.substring(0,2));
                Integer l1m = Integer.parseInt(lesson1.Time.substring(3,5));
                Integer l1time = l1h*60+l1m;

                Integer l2h = Integer.parseInt(lesson2.Time.substring(0,2));
                Integer l2m = Integer.parseInt(lesson2.Time.substring(3,5));
                Integer l2time = l2h*60+l2m;

                return (l1time > l2time)? 1 : ((l1time < l2time)? -1 : 0);
            }
        }
    }

    public interface SwitchToNetwork {
        public void DoToNetwork();
    }
}

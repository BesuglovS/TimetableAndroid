package ru.besuglovs.nu.timetable.fragments;

import android.app.Fragment;
import android.content.res.Resources;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.besuglovs.nu.timetable.Components.NestedListView;
import ru.besuglovs.nu.timetable.ListAdapters.TeacherWeekScheduleAdapter;
import ru.besuglovs.nu.timetable.R;
import ru.besuglovs.nu.timetable.TeacherActivity;
import ru.besuglovs.nu.timetable.apiViews.teacherWeekLesson;
import ru.besuglovs.nu.timetable.view.SlidingTabLayout;

/**
 * Created by Sorry_000 on 18.01.15.
 */
public class TeacherScheduleTabsFragment extends Fragment {
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

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
            // Inflate a new layout from our resources
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.week_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            int week = position + 1;

            TeacherActivity.api.getTeacherWeekSchedule(TeacherActivity.teacherId, week, new Callback<List<teacherWeekLesson>>() {
                @Override
                public void success(List<teacherWeekLesson> weekLessons, Response response) {
                    showTeacherWeekSchedule(view, weekLessons);
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO Grace error handling
                }
            });

            // Return the View
            return view;
        }

        public void showTeacherWeekSchedule(View view, List<teacherWeekLesson> weekLessons)
        {
            Map<Integer, String> dowNames = new HashMap<>();
            dowNames.put(1, "Понедельник");
            dowNames.put(2, "Вторник");
            dowNames.put(3, "Среда");
            dowNames.put(4, "Четверг");
            dowNames.put(5, "Пятница");
            dowNames.put(6, "Суббота");

            Map<Integer, List<teacherWeekLesson>> datedLessons = new HashMap<>();
            for(Integer i = 1; i < 7; i++)
            {
                datedLessons.put(i, new ArrayList<teacherWeekLesson>());
            }

            for (teacherWeekLesson wl : weekLessons)
            {
                datedLessons.get(wl.dow).add(wl);
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
                        Date date = formatIn.parse(datedLessons.get(dow).get(0).Date);
                        dateString = formatOut.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    TextView dowName = new TextView(getActivity());
                    Resources resources = getResources();
                    String dateDowString = String.format(
                            resources.getString(R.string.dowDate), dowNames.get(dow), dateString);
                    dowName.setText(dateDowString);
                    dowName.setBackgroundColor(getResources().getColor(R.color.somewhatBlueGreen));
                    dowName.setTextColor(getResources().getColor(R.color.lightGray));
                    dowName.setGravity(Gravity.CENTER);
                    root.addView(dowName);

                    NestedListView dowListView = new NestedListView(getActivity(), null);
                    TeacherWeekScheduleAdapter adapter =
                            new TeacherWeekScheduleAdapter(datedLessons.get(dow), getActivity().getLayoutInflater());
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
    }

    public interface SwitchNoNetwork {
        void DoNoNetwork();
    }
}

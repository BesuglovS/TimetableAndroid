package ru.besuglovs.nu.timetable.apiInterfaces;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.besuglovs.nu.timetable.apiViews.GroupExam;
import ru.besuglovs.nu.timetable.apiViews.GroupExams;
import ru.besuglovs.nu.timetable.apiViews.teacherWeekLesson;
import ru.besuglovs.nu.timetable.apiViews.weekLesson;
import ru.besuglovs.nu.timetable.timetable.StudentGroup;
import ru.besuglovs.nu.timetable.timetable.Teacher;
import ru.besuglovs.nu.timetable.timetable.Timetable;

/**
 * Created by bs on 30.12.2014.
 */
public interface ScheduleInterface {
    @GET("/api.php?action=weekSchedule")
    public void getGroupWeekSchedule
            (@Query("groupId") int groupId, @Query("week") int week, Callback<List<weekLesson>> response);

    @GET("/api.php?action=list&listtype=studentGroups")
    public void getStudentGroupList(Callback<List<StudentGroup>> response);

    @GET("/api.php?action=bundle")
    public void getWholeSchedule(Callback<Timetable> response);

    @GET("/api.php?action=list&listtype=teachers")
    public void getTeachersList(Callback<List<Teacher>> response);

    @GET("/api.php?action=TeacherWeekSchedule")
    public void getTeacherWeekSchedule
            (@Query("teacherId") int teacherId, @Query("week") int week, Callback<List<teacherWeekLesson>> response);

    @GET("/api.php?action=list&listtype=studentGroups&dbPrefix=old_")
    public void getExamStudentGroupList(Callback<List<StudentGroup>> response);

    @GET("/api.php?action=groupExams&schedulePrefix=old_")
    public void getGroupExams
            (@Query("groupId") int groupId, Callback<Map<Integer, GroupExams>> response);
}

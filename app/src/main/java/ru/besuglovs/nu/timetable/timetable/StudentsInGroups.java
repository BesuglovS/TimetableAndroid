package ru.besuglovs.nu.timetable.timetable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bs on 21.04.2014.
 */
@DatabaseTable(tableName = "studentInGroups")
public class StudentsInGroups {
    @DatabaseField
    public Integer StudentsInGroupsId;
    @DatabaseField
    public Integer StudentId;
    @DatabaseField
    public Integer StudentGroupId;

    public StudentsInGroups() {
    }
}

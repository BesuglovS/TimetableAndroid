package ru.besuglovs.nu.timetable.timetable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bs on 21.04.2014.
 */
@DatabaseTable(tableName = "rings")
public class Ring {
    @DatabaseField
    public Integer RingId;
    @DatabaseField
    public String Time;

    public Ring() {
    }
}

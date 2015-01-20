package ru.besuglovs.nu.timetable.timetable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bs on 21.04.2014.
 */
@DatabaseTable(tableName = "confogOptions")
public class ConfigOption {
    @DatabaseField
    public Integer ConfigOptionId;
    @DatabaseField
    public String Key;
    @DatabaseField
    public String Value;

    public ConfigOption() {
    }
}

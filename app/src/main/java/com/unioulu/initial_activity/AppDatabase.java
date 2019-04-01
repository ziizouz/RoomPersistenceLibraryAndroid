package com.unioulu.initial_activity;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {UsersTable.class,
        EmergencySettingsTable.class,
        OtherSettingsTable.class,
        Medicines.class,
        MedicineStatistics.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsersTableInterface usersTableInterface();
    public abstract MedicineDBInterface medicineDBInterface();
    public abstract MedicineStatisticsInterface medicineStatisticsInterface();
    public abstract OtherSettingsInterface otherSettingsInterface();
    public abstract EmergencySettingsInterface emergencySettingsInterface();
}


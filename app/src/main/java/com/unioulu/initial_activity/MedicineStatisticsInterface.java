package com.unioulu.initial_activity;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.unioulu.initial_activity.Medicines;

import java.util.List;

@Dao
public interface MedicineStatisticsInterface {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDateAndStatus(MedicineStatistics medicineStatisticsElement);

    @Query("SELECT * FROM MedicineStatistics WHERE id = :id")
    List<MedicineStatistics> fetchMedicineStatisticsByID(int id);


    @Query("DELETE FROM MedicineStatistics")
    void deleteMedicinesStatisticsTable();

}

package com.unioulu.initial_activity;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    // Global variables
    private TextView textView;
    private EditText editText;
    private static final String DATABASE_NAME = "medicines_db";
    private static final String TAG_DB        = "DB";
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        // Building the database at onCreate
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

    }


    public void RemoveClicked(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Deleting all elements in Medicines table
                appDatabase.medicineDBInterface().deleteMedicinesTable();
                Log.d(TAG_DB, "Medicines table dropped !");
                // Deleting all elements in Medicines Statistics table
                appDatabase.medicineStatisticsInterface().deleteMedicinesStatisticsTable();
                Log.d(TAG_DB, "Medicines Statistics table dropped !");
            }
        }).start();

    }

    public void AddClicked(View view){

        final String medicine_name = editText.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Adding user

                // Adding a medicine
                Medicines medicine = new Medicines(
                        medicine_name,
                        medicine_name+"_picture_URL",
                        "8:00",
                        "12:00",
                        "NaN",
                        "Nan"
                );

                appDatabase.medicineDBInterface().insertOnlySingleMedicine(medicine);
                Log.d(TAG_DB, "medicine inserted !");

                // Adding medicine statistic information
                MedicineStatistics medicineStatisticsElement = new MedicineStatistics(
                        Calendar.getInstance().getTime().getTime(),     // Date and time (Long format)
                        "100"   // Status string
                );

                appDatabase.medicineStatisticsInterface().insertDateAndStatus(medicineStatisticsElement);
                Log.d(TAG_DB, "Medicine statistic information added !");
            }
        }).start();

    }

    public void PrintAllClicked(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Medicines medicine;

                medicine = appDatabase.medicineDBInterface().fetchOneMedicineByName("paracetamol");

                Log.d(TAG_DB, "medicine retreived: " + medicine.toString());

                List<MedicineStatistics> statOfMidicine = appDatabase.medicineStatisticsInterface().fetchMedicineStatisticsByID(medicine.getId());

                for (int i=0; i< statOfMidicine.size(); i++){
                    Log.d(TAG_DB, statOfMidicine.get(i).toString());
                    Long date = statOfMidicine.get(i).getDate();
                    String dateString = DateFormat.format("MM/dd/yyyy", new Date(date)).toString();
                    Log.d(TAG_DB, "Date: "+ dateString);
                }

            }
        }).start();

    }
}

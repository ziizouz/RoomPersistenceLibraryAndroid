package com.unioulu.initial_activity;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                // Deleting all users
                appDatabase.usersTableInterface().deleteUserTable();
                Log.d(TAG_DB, "Users table deleted !");
                // Deleting all settings
                appDatabase.otherSettingsInterface().deleteAllOtherSettings();
                Log.d(TAG_DB, "OtherSettings table dropped !");
                // Deleting all emergency contacts
                appDatabase.emergencySettingsInterface().deleteAllEmergencyContacts();
                Log.d(TAG_DB, "Emergency settings dropped !");

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
                UsersTable user = new UsersTable(
                        "Antti",
                        "antii.fi@gmail.com",
                        "1234password"
                );

                appDatabase.usersTableInterface().createUser(user);

                // Adding Emergency settings contacts
                EmergencySettingsTable emergencyContact = new EmergencySettingsTable(
                        "my son",
                        "+358 42 4210 012",
                        "storage/0/Pictures/my_sons_pic.png"
                );
                appDatabase.emergencySettingsInterface().insertEmergencyContact(emergencyContact);

                // Adding settings
                // ---------------------------------- Generating some fake settings time -----------------
                // Vars only for testing purposes
                String[] strings_time   ={" 8:30:00",
                                     "13:00:00",
                                     "19:30:00",
                                     "22:00:00"};

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

                Long[] long_time = new Long[4];
                try{
                    for (int i=0; i< strings_time.length; i++){
                        Date date = sdf.parse(strings_time[i]);
                        long_time[i] = date.getTime();
                    }


                }catch (ParseException e){
                    e.printStackTrace();
                }
                // --------------------------------- End of fake settings time generation --------------------------
                // Inserting new settings only if no previous settings are found in the table (Otherwise use update)
                if (appDatabase.otherSettingsInterface().fetchAllOtherSettings().size() == 0){
                    OtherSettingsTable otherSettings = new OtherSettingsTable(
                            long_time[0],
                            long_time[1],
                            long_time[2],
                            long_time[3],
                            "5"
                    );

                    appDatabase.otherSettingsInterface().insertOtherSettings(otherSettings);

                }else{ // If table already contains settings, just update it
                    OtherSettingsTable otherSettings = new OtherSettingsTable(
                            long_time[0],
                            long_time[1],
                            long_time[2],
                            long_time[3],
                            "10"
                    );

                    appDatabase.otherSettingsInterface().updateOtherSettings(otherSettings);
                }

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

                // Printing user
                List<UsersTable> users = appDatabase.usersTableInterface().fetchAllUsers();
                for (UsersTable user : users){
                    Log.d(TAG_DB, "Users");
                    Log.d(TAG_DB, user.toString());
                }

                // Priting settings
                List<OtherSettingsTable> otherSettings = appDatabase.otherSettingsInterface().fetchAllOtherSettings();
                for (OtherSettingsTable setting: otherSettings){
                    Log.d(TAG_DB, "OtherSettings");
                    Log.d(TAG_DB, setting.toString());
                }

                // Priting emergency contacts
                List<EmergencySettingsTable> emergencySettingsContacts = appDatabase.emergencySettingsInterface().fetchAllEmergencyContacts();
                for (EmergencySettingsTable contact: emergencySettingsContacts){
                    Log.d(TAG_DB, "Emergency contact");
                    Log.d(TAG_DB, contact.toString());
                }

                // Printing a single medicine
                medicine = appDatabase.medicineDBInterface().fetchOneMedicineByName("paracetamol");

                Log.d(TAG_DB, "medicine retreived: " + medicine.toString());


                // Printing out medicines' statistics
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

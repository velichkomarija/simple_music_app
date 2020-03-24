package com.velichkomarija4.simplemusicapp;

import com.velichkomarija4.simplemusicapp.db.DataBase;

import androidx.room.Room;

public class Application extends android.app.Application {

    private DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();

        dataBase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "music_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public DataBase getDataBase() {
        return dataBase;
    }
}

package com.englishlearn.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.englishlearn.myapplication.data.source.local.DbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
        SQLiteDatabase sqLiteDatabase = new DbHelper(this.getApplicationContext()).getWritableDatabase();

    }
}

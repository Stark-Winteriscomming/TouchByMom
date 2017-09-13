package com.example.changho.changholock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        //
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, 2);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "subject");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "blabla");

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);


        /// read

        db = mDbHelper.getReadableDatabase();
        Cursor c =  db.query(FeedReaderContract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
        Log.d("testing ","_id value "+itemId);
    }


}

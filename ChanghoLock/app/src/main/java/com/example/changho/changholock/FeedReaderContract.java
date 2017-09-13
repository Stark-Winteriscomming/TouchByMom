package com.example.changho.changholock;

import android.provider.BaseColumns;

/**
 * Created by Changho on 2016-09-19.
 */
public class FeedReaderContract {
    public FeedReaderContract() {
    }
    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}

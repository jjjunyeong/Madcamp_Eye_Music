package com.example.myapplication;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns{
        public static final String NAME = "name";
        public static final String NUMBER = "number";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +NAME+" text not null , "
                +NUMBER+" text not null );";
    }
}

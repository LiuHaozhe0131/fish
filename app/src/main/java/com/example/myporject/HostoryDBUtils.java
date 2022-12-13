package com.example.myporject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HostoryDBUtils {
    public static final String DB_NAME = "history_dbname";

    public static final int VERSION = 1;

    private static HostoryDBUtils sqliteDB;

    private SQLiteDatabase db;

    private HostoryDBUtils(Context context) {
        HistoryHelper dbHelper = new HistoryHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }


    public synchronized static HostoryDBUtils getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new HostoryDBUtils(context);
        }
        return sqliteDB;
    }


    public void delete(Context context,String id) {
        HistoryHelper dbHelper = new HistoryHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getReadableDatabase();
        db.delete("History", "uid=?", new String[] { id });
    }
    public void change(Context context, History history) {
        HistoryHelper dbHelper = new HistoryHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", history.getId());
        values.put("uid", history.getUid());
        values.put("pic", history.getPic());
        values.put("checks", history.getCheck());
        values.put("diff", history.getDiff());
        values.put("type", history.getType());

        db.update("History", values, "uid=?", new String[]{history.getUid()+""});
    }


    public void  save(History user) {
        try {
            db.execSQL("insert into History(pic,checks,type,diff,id) values(?,?,?,?,?) ", new String[]{
                    user.getPic()+"",
                    user.getCheck()+"",
                    user.getType(),
                    user.getDiff(),
                    user.getId()+"",


            });
        } catch (Exception e) {
            Log.d("����", e.getMessage().toString());
        }
    }
    @SuppressLint("Range")
    public List<History> load(String type){
        List<History> list = new ArrayList<>();
        Cursor cursor = db
                .query("History", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (type.equals(cursor.getString(cursor.getColumnIndex("type")))){
                    History user = new History();
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
                    user.setCheck(cursor.getString(cursor.getColumnIndex("checks")));
                    user.setType(cursor.getString(cursor.getColumnIndex("type")));
                    user.setDiff(cursor.getString(cursor.getColumnIndex("diff")));
                    user.setPic(cursor.getInt(cursor.getColumnIndex("pic")));
                    list.add(user);
                }

            } while (cursor.moveToNext());
        }
        return list;
    }
}

package com.example.assistme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "assistme";
    private static final String TABLE_Notes = "notesdetails";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";

    public DbHandler(Context context){

        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Notes + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_BODY + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Notes);
        // Create tables again onCreate(db);
    }

    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Add new note details
    void insertNoteDetails(String title, String body) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_TITLE, title);
        cValues.put(KEY_BODY, body);
        db.insert(TABLE_Notes,null, cValues);
        db.close();
    }

    // Get all note details
    public ArrayList<HashMap<String, String>> getNotes() {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> noteList = new ArrayList<>();
        String query = "SELECT id, title, body FROM "+ TABLE_Notes;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            HashMap<String,String> note = new HashMap<>();
            note.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
            note.put("title",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            note.put("body",cursor.getString(cursor.getColumnIndex(KEY_BODY)));
            noteList.add(note);
        }

        return noteList;
    }

    // Get note details based on noteid
    public ArrayList<HashMap<String, String>> GetNoteByNoteId(int noteid){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> noteList = new ArrayList<>();
        String query = "SELECT title, body FROM "+ TABLE_Notes;
        Cursor cursor = db.query(TABLE_Notes, new String[]{KEY_TITLE, KEY_BODY}, KEY_ID+ "=?", new String[]{String.valueOf(noteid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> note = new HashMap<>();
            note.put("name",cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            note.put("designation",cursor.getString(cursor.getColumnIndex(KEY_BODY)));
            noteList.add(note);
        }

        return noteList;
    }

    // Update note details
    public int updateNoteDetails(String title, String body, int noteid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_TITLE, title);
        cVals.put(KEY_BODY, body);
        int count = db.update(TABLE_Notes, cVals, KEY_ID+" = ?",new String[]{String.valueOf(noteid)});
        return count;
    }

    // Delete note details by noteid
    public void deleteNote(int noteid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Notes, KEY_ID+" = ?", new String[]{String.valueOf(noteid)});
        db.close();
    }

    // Delete all notes
    public void deleteAllNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Notes, null, null);
        db.close();
    }
}

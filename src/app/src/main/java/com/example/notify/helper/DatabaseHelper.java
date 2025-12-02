package com.example.notify.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.notify.entity.Notify;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    public static final String DATABASE_NAME = "NotifyDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tbl_notes";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_DATETIME_CREATED = "datetime_created";
    public static final String COL_DATETIME_MODIFIED = "datetime_modified";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_CONTENT + " TEXT, " +
                COL_DATETIME_CREATED + " TEXT, " +
                COL_DATETIME_MODIFIED + " TEXT);";
        db.execSQL(query);
    }

    public long addNote(Notify n) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, n.getTitle());
        cv.put(COL_CONTENT, n.getContent());
        cv.put(COL_DATETIME_CREATED, n.getDateTimeCreated());
        cv.put(COL_DATETIME_MODIFIED, n.getDateTimeModified());
        return db.insert(TABLE_NAME, null, cv);
    }

    public Cursor readAllNotes() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        return db != null ? db.rawQuery(query, null) : null;
    }

    public long updateNoteById(Notify note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_TITLE, note.getTitle());
        cv.put(COL_CONTENT, note.getContent());
        cv.put(COL_DATETIME_MODIFIED, note.getDateTimeModified());

        return db.update(TABLE_NAME, cv, "id=?",new String[]{note.getId()+""});

    }

    public int deleteNotebyId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id=?", new String[]{id});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

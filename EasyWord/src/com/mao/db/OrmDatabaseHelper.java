package com.mao.db;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mao.bean.Note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper{

	/** 数据库名 */
	private final static String DB_NAME = "easyNote.db";
	/** 数据库版本 */
	private final static int DB_VERSION = 1;
	
	private Dao<Note, Integer> noteDao;
	
	public OrmDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public OrmDatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(getConnectionSource(), Note.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
	}
	
	public Dao<Note, Integer> getNoteDao() {
		if(noteDao == null) {
			try {
				noteDao = getDao(Note.class);
			} catch (Exception e) {
				e.printStackTrace();
				noteDao = null;
			}
		}
		return noteDao;
	}
}

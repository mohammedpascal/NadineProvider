package com.mohammedpascal.nadine;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Content provider for advice continents
 * 
 * @author Mohammed Mustafa
 * 
 */
public abstract class BeanProvider extends ContentProvider {

	private static final Map<String, Uri> uris = new HashMap<String, Uri>();
	
	/**
	 * Provider authority
	 */
	private String authority;

	/**
	 * Provider content URI
	 */
	private Uri contentUri;

	/**
	 * Auto increment to be used a primary key
	 */
	public static final String _ID = "_id";

	/**
	 * This constant will be used with multiple rows query
	 */
	private static final int ROWS = 1;

	/**
	 * 
	 */
	private static final int ROW_ID = 2;

	private UriMatcher uriMatcher;

	/**
	 * Variable to hold SQLite database reference
	 */
	private SQLiteDatabase db;

	/**
	 * Current database version
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * Create table statement
	 */
	private String databaseCreate;

	private String table;
	
	public abstract Class<?> getEntity();
	
	public void init() {
		Class<?> clazz = getEntity();
		String[] columns = Reflect.fields(clazz);
		
		table = getClass().getSimpleName();
		authority = getContext().getPackageName() + "."+ table;
		contentUri = Uri.parse("content://" + authority + "/" + table);
		
		uris.put(clazz.getName(), contentUri);
		
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(authority, table, ROWS);
		uriMatcher.addURI(authority, table + "/#", ROW_ID);

		// build create statement
		databaseCreate = "create table " + table + " ( " + _ID
				+ " integer primary key ";
		for (String column : columns) {
			databaseCreate += ", " + column + " text ";
		}

		databaseCreate += ");";
	}
	
	public Uri uri(Object o){
		return uri(o.getClass());
	}

	public static Uri uri(Class<?> clazz) {
		return uris.get(clazz.getName());
	}

	/**
	 * SQLite helper
	 * 
	 * @author Mohammed Mustafa
	 * 
	 */
	private class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, table + ".sqlite", null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(databaseCreate);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Content provider database",
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + table);
			onCreate(db);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	// Implementation of content provider methods
	// //////////////////////////////////////////////////////////////////////

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int count = 0;
		switch (uriMatcher.match(uri)) {
		case ROWS:
			count = db.delete(table, selection, selectionArgs);
			break;
		case ROW_ID:
			String id = uri.getPathSegments().get(1);
			count = db.delete(table,
					_ID
							+ " = "
							+ id
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		// ---get all records---
		case ROWS:
			return "vnd.android.cursor.rows/" + authority;
			// ---get a particular record---
		case ROW_ID:
			return "vnd.android.cursor.row/" + authority;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// ---add a new record---
		long rowID = db.insert(table, "", values);

		// ---if added successfully---
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(contentUri, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);

			return _uri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		init();
		Context context = getContext();
		Bean.getInstance().init(context);
		DatabaseHelper dbHelper = new DatabaseHelper(context);

		db = dbHelper.getWritableDatabase();

		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(table);

		if (uriMatcher.match(uri) == ROW_ID) {
			// ---if getting a particular record---
			sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
		}

		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);

		// ---register to watch a content URI for changes---
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ROWS:
			count = db.update(table, values, selection, selectionArgs);
			break;
		case ROW_ID:
			count = db.update(
					table,
					values,
					_ID
							+ " = "
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

}
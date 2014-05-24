package com.mohammedpascal.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public final class DB {

	private static DB db = new DB();

	private ContentResolver contentResolver;
	
	private DB() {}

	public static DB getInstance() {
		return db;
	}

	/**
	 * To be used for context initialization
	 * 
	 * @param context
	 */
	public void init(Context context) {
		contentResolver = context.getContentResolver();
	}

	/**
	 * Returns content resolver
	 * 
	 * @return content resolver
	 */
	public ContentResolver getContentResolver() {
		return contentResolver;
	}

	/**
	 * Closes Cursor safely
	 * 
	 * @param cursor
	 *            the cursor to be closed
	 */
	public static void close(Cursor cursor) {
		try {
			cursor.close();
		} catch (Exception e) {
		}
	}

	// db operations using class instead of Content URI

	public static Uri insert(Class<?> clazz, ContentValues values) {
		return db.getContentResolver().insert(NadineProvider.uri(clazz), values);
	}

	public static int delete(Class<?> clazz, String where, String[] args) {
		return db.getContentResolver().delete(NadineProvider.uri(clazz), where, args);
	}

	public static int update(Class<?> clazz, ContentValues values, String where, String[] args) {
		return db.getContentResolver().update(NadineProvider.uri(clazz), values, where, args);
	}
	
	public static Cursor query(Class<?> clazz, String[] select, String where, String[] args, String order) {
		return db.getContentResolver().query(NadineProvider.uri(clazz), select, where, args, order);
	}
	
	//Versions of query method with different parameters

	public static Cursor query(Class<?> clazz) {
		return query(clazz, null, null, null , null);
	}
	
	public static Cursor query(Class<?> clazz, String order) {
		return query(clazz, null, null, null , order);
	}
	
	public static Cursor query(Class<?> clazz, String column, String value) {
		return query(clazz, null, column+" = ? ", new String[]{ value }, null);
	}
	
	public static Cursor query(Class<?> clazz, String where, String[] args) {
		return query(clazz, null, where, args, null);
	}
	
	public static Cursor query(Class<?> clazz, String where, String[] args, String order) {
		return query(clazz, null, where, args, order);
	}
	
	public static Cursor query(Class<?> clazz, String[] select, String where, String[] args) {
		return query(clazz, select, where, args, null);
	}
	
	//Versions of update with different parameters
	
	public static int update(Class<?> clazz, ContentValues values) {
		return update(clazz, values, null, new String[]{} );
	}
	
	public static int update(Class<?> clazz, ContentValues values, String column, String value) {
		return update(clazz, values, column+" = ? ", new String[]{ value} );
	}
	
	//Versions of delete with different parameters
	
	public static int delete(Class<?> clazz) {
		return delete(clazz, null, new String[]{});
	}
	
	public static int delete(Class<?> clazz, String column, String value) {
		return delete(clazz, column+" = ? ", new String[]{ value });
	}
	
	//helpers
	
	public static int getInt(Cursor cursor, String columnName) {
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}
	
	public static float getFloat(Cursor cursor, String columnName) {
		return cursor.getFloat(cursor.getColumnIndex(columnName));
	}
	
	public static double getDouble(Cursor cursor, String columnName) {
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}
	
	public static byte[] getBlob(Cursor cursor, String columnName) {
		return cursor.getBlob(cursor.getColumnIndex(columnName));
	}
	
	public static long getLong(Cursor cursor, String columnName) {
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
	
	public static short getShort(Cursor cursor, String columnName) {
		return cursor.getShort(cursor.getColumnIndex(columnName));
	}
	
	public static String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

}

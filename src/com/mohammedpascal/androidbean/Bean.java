package com.mohammedpascal.androidbean;

import java.util.Collection;

import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public final class Bean {

	/**
	 * The context of android application
	 */
	private Context context;

	private static Bean bean = new Bean();

	private Bean() {
	}

	public static Bean getInstance() {
		return bean;
	}

	/**
	 * To be used for context initialization
	 * 
	 * @param context
	 */
	public void init(Context context) {
		this.context = context;
	}

	/**
	 * Returns the current context
	 * 
	 * @return current context
	 */
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Returns content resolver
	 * 
	 * @return content resolver
	 */
	public ContentResolver getContentResolver() {
		return context.getContentResolver();
	}

	// /////////////// query class ///////////////

	public static Cursor query(Class<?> clazz) {
		return query(BeanProvider.uri(clazz), null, null, null, null);
	}

	public static Cursor query(Class<?> clazz, String column, String value) {
		return query(BeanProvider.uri(clazz), null, column + " = ? ",
				new String[] { value }, null);
	}

	public static Cursor query(Class<?> clazz, String where, String[] args) {
		return query(BeanProvider.uri(clazz), null, where, args, null);
	}

	public static Cursor query(Class<?> clazz, String[] select, String where,
			String[] args) {
		return query(BeanProvider.uri(clazz), select, where, args, null);
	}

	public static Cursor query(Class<?> clazz, String where, String[] args,
			String order) {
		return query(BeanProvider.uri(clazz), null, where, args, order);
	}

	public static Cursor query(Class<?> clazz, String[] select, String where,
			String[] args, String order) {
		return query(BeanProvider.uri(clazz), select, where, args, order);
	}

	///////////////// save ////////////////

	public static void save(Entity e) throws IllegalAccessException,
			IllegalArgumentException, NoSuchFieldException {
		String _id = e.get_id();
		if (_id == null) {
			Bean.insert(BeanProvider.uri(e.getClass()),
					Convert.toContentValues(e));
		} else {
			Bean.update(BeanProvider.uri(e.getClass()),
					Convert.toContentValues(e), " _id = ? ",
					new String[] { _id });
		}
	}

	////////////// update //////////////////////
	
	public static int update(Class<?> clazz, ContentValues values, String where, String[] args) throws JSONException {
		return update(BeanProvider.uri(clazz), values, where, args);
	}
	
	////////////// delete //////////////////////

	public static int delete(Entity e) throws IllegalAccessException,
			IllegalArgumentException, NoSuchFieldException {
		return delete(e.getClass(), e.get_id());
	}

	public static int delete(Class<?> clazz, String id) {
		return delete(clazz, "_id", id);
	}

	public static void delete(Class<?> clazz, Collection<?> ids) {
		for (Object id : ids) {
			delete(clazz, id.toString());
		}
	}

	public static int delete(Collection<Entity> c)
			throws IllegalAccessException, IllegalArgumentException,
			NoSuchFieldException {
		for (Entity e : c) {
			return delete(e);
		}

		return 0;
	}

	public static int delete(Class<?> clazz) {
		return delete(clazz, null, new String[] {});
	}

	public static int delete(Class<?> clazz, String column, String value) {
		return delete(clazz, column + " = ? ", new String[] { value });
	}

	public static int delete(Class<?> clazz, String where, String[] args) {
		return delete(BeanProvider.uri(clazz), where, args);
	}

	public static String get(Cursor cursor, String column) {
		return cursor.getString(cursor.getColumnIndex(column));
	}

	public static <T> T find(Class<T> clazz, String id) throws Exception {
		Cursor cursor = null;
		try {
			cursor = query(clazz, "_id", id);
			if (cursor.moveToNext()) {
				return Convert.toObject(clazz, cursor);
			}
		} catch (Exception e) {
		} finally {
			close(cursor);
		}

		throw new Exception("Object not found");
	}

	public static <T> Query<T> createQuery(Class<T> clazz) {
		return new Query<T>(clazz);
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

	// ----------------------

	private static Uri insert(Uri url, ContentValues values) {
		return bean.getContentResolver().insert(url, values);
	}

	private static Cursor query(Uri uri, String[] select, String where,
			String[] args, String order) {
		return bean.getContentResolver().query(uri, select, where, args, order);
	}

	private static int delete(Uri uri, String where, String[] args) {
		return bean.getContentResolver().delete(uri, where, args);
	}

	private static int update(Uri uri, ContentValues values, String where,
			String[] args) {
		return bean.getContentResolver().update(uri, values, where, args);
	}

}

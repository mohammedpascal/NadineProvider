package com.mohammedpascal.nadine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class Convert {

	public static JSONObject toJson(Object o) throws Exception {
		Field[] fields = o.getClass().getDeclaredFields();
		JSONObject json = new JSONObject();
		for (Field field : fields) {
			field.setAccessible(true);
			json.put(field.getName(), field.get(o));
		}
		
		return json;
	}
	
	public static <T> T fromJson(Class<T> clazz, JSONObject obj) throws IllegalAccessException, IllegalArgumentException, InstantiationException{
		return fromJson(clazz.newInstance(), obj);
		
	}
	
	public static <T> T fromJson(T o, JSONObject obj) throws IllegalAccessException, IllegalArgumentException{
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(o, obj.opt(field.getName()));
		}
		
		return o;
	}
	
	public static ContentValues toContentValues(Object obj) throws IllegalAccessException, IllegalArgumentException {
		ContentValues values = new ContentValues();
		Field[] fields = Reflect.getFields(obj);
		for (Field field : fields) {
			field.setAccessible(true);
			values.put(field.getName(), field.get(obj).toString());
		}

		return values;
	}
	
	public static ContentValues toContentValues(JSONObject obj)
			throws JSONException {
		ContentValues values = new ContentValues();

		JSONArray fields = obj.names();
		for (int i = 0; i < fields.length(); i++) {
			String field = fields.getString(i);
			values.put(field, obj.getString(field));
		}

		return values;
	}
	
	public static ContentValues toContentValues(JSONObject obj,
			String[] columns) throws JSONException {
		ContentValues values = new ContentValues();
		for (String column : columns) {
			try {
				values.put(column, obj.getString(column));
			} catch (Exception e) {
				values.put(column, "");
			}
		}

		return values;
	}
	
	public static JSONObject toJSONObject(Cursor c) throws JSONException {
		return toJSONObject(c, c.getColumnNames());
	}

	public static JSONObject toJSONObject(Cursor c, String[] columns)
			throws JSONException {
		JSONObject obj = new JSONObject();
		for (int i = 0; i < columns.length; i++) {
			obj.put(columns[i], c.getString(c.getColumnIndex(columns[i])));
		}

		return obj;
	}
	
	public static JSONArray toJSONArray(Cursor c) throws JSONException {
		return toJSONArray(c, Integer.MAX_VALUE);
	}
	
	public static JSONArray cursorToJSONArray(Cursor c, String[] columns) throws JSONException {
		return toJSONArray(c, columns, Integer.MAX_VALUE);
	}

	public static JSONArray toJSONArray(Cursor c, int limit)
			throws JSONException {
		String[] columns = c.getColumnNames();
		return toJSONArray(c, columns, limit);
		
	}

	public static JSONArray toJSONArray(Cursor c, String[] columns, int limit) throws JSONException {
		JSONArray result = new JSONArray();
		
		while (c.moveToNext() && limit > 0) {
			result.put(toJSONObject(c, columns));
			limit--;
		}

		return result;
	}
	
	public static <T> T toObject(Class<T> clazz, Cursor cursor) throws IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException, NoSuchMethodException{
		return fromCursor(clazz.newInstance(), cursor);
	}
	
	public static <T> List<T> toObjects(Class<T> clazz, Cursor cursor ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		List<T> list = new ArrayList<T>();
		while( cursor.moveToNext() ){
			list.add(toObject(clazz, cursor));
		}
		
		return list;
	}
	
	public static <T> T fromCursor(T o, Cursor cursor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(o, Bean.get(cursor, field.getName()));
		}
		
		o.getClass().getMethod("set_id", String.class).invoke(o, Bean.get(cursor, "_id"));
		
		return o;
	}

}

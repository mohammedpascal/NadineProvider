package com.mohammedpascal.nadine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class Entity {

	private long _id;
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}
	
	public void read(JSONObject obj) throws IllegalAccessException, IllegalArgumentException, JSONException{
		fromJson(this, obj);	
	}
	
	public void read(Cursor cursor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		fromCursor(this, cursor);
	}
	
	public void save() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
		Bean.save(this);
	}
	
	public int delete(){
		return Bean.delete(getClass(), _id);
	}
	
	public JSONObject toJson() throws Exception {
		Field[] fields = getClass().getDeclaredFields();
		JSONObject json = new JSONObject();
		for (Field field : fields) {
			field.setAccessible(true);
			json.put(field.getName(), field.get(this));
		}
		
		return json.put("_id", _id);
	}
	
	public static <T> T fromJson(T o, JSONObject obj) throws IllegalAccessException, IllegalArgumentException, JSONException{
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			
			if( field.getType().equals(byte.class) ){
				field.setByte(o, (byte)obj.getInt(field.getName()));
			}else if( field.getType().equals(short.class) ){
				field.setShort(o, (short) obj.getInt(field.getName()));
			}else if( field.getType().equals(int.class) ){
				field.setInt(o, (Integer)obj.getInt(field.getName()));
			}else if( field.getType().equals(long.class) ){
				field.setLong(o, obj.getLong(field.getName()));
			}else if( field.getType().equals(float.class) ){
				field.setFloat(o, (float) obj.getDouble(field.getName()));
			}else if( field.getType().equals(double.class) ){
				field.setDouble(o, obj.getDouble(field.getName()));
			}else if( field.getType().equals(char.class) ){
				field.setChar(o, (char) obj.getInt(field.getName()));;
			}else if( field.getType().equals(String.class)){
				field.set(o, obj.getString(field.getName()));
			}else if( field.getType().equals(boolean.class) ){
				field.setBoolean(o, obj.getBoolean(field.getName()));
			}else if( field.getType().equals(Byte.class) ){
				field.set(o, Byte.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Short.class) ){
				field.set(o, Short.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Integer.class) ){
				field.set(o, Integer.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Long.class) ){
				field.set(o, Long.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Float.class) ){
				field.set(o, Byte.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Double.class) ){
				field.set(o, Double.valueOf(obj.getString(field.getName())));
			}else if( field.getType().equals(Character.class) ){
				field.set(o, Character.valueOf((char) obj.getInt(field.getName())));
			}else if( field.getType().equals(Boolean.class) ){
				field.set(o, Boolean.valueOf(obj.getString(field.getName())));
			}else{
				field.set(o, obj.get(field.getName()));
			}
		}
		
		return o;
	}
	
	public static <T> T fromCursor(T o, Cursor cursor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException{
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			
			if( field.getType().equals(byte.class) ){
				field.setByte(o, (byte)cursor.getInt(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(short.class) ){
				field.setShort(o, cursor.getShort(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(int.class) ){
				field.setInt(o, cursor.getInt(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(long.class) ){
				field.setLong(o, cursor.getInt(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(float.class) ){
				field.setFloat(o, cursor.getFloat(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(double.class) ){
				field.setDouble(o, cursor.getDouble(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(char.class) ){
				field.setChar(o, (char)cursor.getInt(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(String.class) ){
				field.set(o, cursor.getString(cursor.getColumnIndex(field.getName())));
			}else if( field.getType().equals(boolean.class) ){
				field.setBoolean(o, cursor.getInt(cursor.getColumnIndex(field.getName()))== 1 );
			}else if( field.getType().equals(Byte.class) ){
				field.set(o, Byte.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(Short.class) ){
				field.set(o, Short.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(Integer.class) ){
				field.set(o, Integer.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(Long.class) ){
				field.set(o, Long.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(float.class) ){
				field.set(o, Float.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(double.class) ){
				field.set(o, Double.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(Character.class) ){
				field.set(o, Character.valueOf((char) cursor.getInt(cursor.getColumnIndex(field.getName()))));
			}else if( field.getType().equals(boolean.class) ){
				field.set(o, Boolean.valueOf(cursor.getString(cursor.getColumnIndex(field.getName()))));
			}else{
				field.set(o,null);
			}
		}
		
		o.getClass().getMethod("set_id", long.class).invoke(o, cursor.getLong(cursor.getColumnIndex("_id")));
		
		return o;
	}
	
}

package com.mohammedpascal.androidbean;

import java.lang.reflect.Field;
import org.json.JSONObject;

import android.database.Cursor;

public class Entity {

	private String _id;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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
	
	public void read(JSONObject obj) throws IllegalAccessException, IllegalArgumentException{
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(this, obj.opt(field.getName()));
		}		
	}
	
	public void read(Cursor cursor) throws IllegalAccessException, IllegalArgumentException{
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(this, Bean.get(cursor, field.getName()));
		}		
	}
	
	public void save() throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException{
		Bean.save(this);
	}
	
	public int delete(){
		return Bean.delete(getClass(), _id);
	}
	
}

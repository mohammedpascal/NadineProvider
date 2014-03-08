package com.mohammedpascal.nadine;

import java.util.List;

import android.database.Cursor;


public class Query<T>{
	
	private Class<T> clazz;
	private String where;
	private String[] args;
	private String order;

	public Query(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T> Query<T> use(Class<T> clazz){
		return new Query<T>(clazz);
	}
	
	public Query<T> order(String order){
		this.order = order;
		return this;
	}
	
	public Query<T> args(String[] args){
		this.args = args;
		return this;
	}
	
	public Cursor get(){
		return get(where);
	}
	
	public Cursor get(String where){
		return get(where, args);
	}
	
	public Cursor get(String where, String[] args){
		return get(where, args, order);
	}
	
	public Cursor get(String where, String[] args, String order){
		return Bean.query(clazz, where, args, order);
	}
	
	public Query<T> where(String where){
		return where(where, args);
	}
	
	public Query<T> where(String column, String value){
		return where(column+" = ? ", new String[]{ value });
	}
	
	public Query<T> where(String where, String[] args){
		return where(where, args, order);
	}
	
	public Query<T> where(String where, String[] args, String order){
		this.args = args;
		this.where = where;
		this.order = order;
		return this;
	}
	
	
	public List<T> findList() throws Exception{
		Cursor cursor = null;
		try{
			cursor = Bean.query(clazz, where, args, order);
			return Convert.toObjects(clazz, cursor);
		}catch(Exception e){
			throw e;
		}finally{
			Bean.close(cursor);
		}
		
	}
	
	public List<T> findList(String where) throws Exception{
		this.where = where;
		return findList();
	}
	
	public List<T> findList(String where, String[] args) throws Exception{
		this.args = args;
		return findList(where);
	}
	
	public List<T> findList(String where, String[] args, String order) throws Exception{
		this.order = order;
		return findList(where, args);
	}
	
}



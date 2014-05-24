package com.mohammedpascal.provider;

import com.mohammedpascal.nadine.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			
			//Insert
			ContentValues values = new ContentValues();
			values.put("username", "mohammed");
			values.put("password", "123");
			DB.insert(SampleProvider.class, values);
			
			//Query
			Cursor cursor = DB.query(SampleProvider.class);
			
			while( cursor.moveToNext() ){
				String _id = DB.getString(cursor, "_id");
				String username = DB.getString(cursor, "username");
				String password = DB.getString(cursor, "password");
				System.out.println(_id+" "+username+" "+password);
			}
			
			cursor.close();
			
			//Update
			ContentValues changes = new ContentValues();
			changes.put("password", "111");
			DB.update(SampleProvider.class, changes, "username", "mohammed");
			
			//Delete
			DB.delete(SampleProvider.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}


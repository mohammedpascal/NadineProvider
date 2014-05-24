NADINE
======

A very easy content provider for Android

# How it works?

Extend NadineProvider and implements it's single method getColumns:

```Java
public class SampleProvider extends NadineProvider {
	public String[] getColumns(){
		return new String[]{ "username", "password" };
	}
}
```

# How to use it?

```Java
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
```

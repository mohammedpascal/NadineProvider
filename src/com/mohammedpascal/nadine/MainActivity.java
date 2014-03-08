package com.mohammedpascal.nadine;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);//
		
		try {
			//insert
			User user = new User();
			user.setName("mohammed");
			user.setPhone("0762355311");
			//user.save();
			
			//update
			//User u = Bean.find(User.class, "2");
			//u.setPhone("123");
			//u.save();
		
			//delete
			//u.delete();
			
			//Bean.delete(User.class, "4");
			
			
			Query<User> query = Bean.createQuery(User.class);
			List<User> list = query.findList();
			for (User user2 : list) {
				System.out.println(user2.toJson());
			}
			
			//System.out.println(Convert.toJSONArray(Bean.query(User.class)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

}


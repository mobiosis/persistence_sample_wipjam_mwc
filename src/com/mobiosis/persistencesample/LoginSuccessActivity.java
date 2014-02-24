package com.mobiosis.persistencesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobiosis.persistencesample.data.Globals;
import com.mobiosis.persistencesample.data.MyPreferences;
import com.mobiosis.persistencesample.model.MyUser;
import com.mobiosis.persistencesample.model.MyUser.Gender;

public class LoginSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login_success);
		
		findViewById(R.id.button_continue).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//obtatining the user data from the intent
				String name = getIntent().getStringExtra("MyUser.name");
				int age = getIntent().getIntExtra("MyUser.age", 0);
				Gender gender = (Gender) 
						getIntent().getSerializableExtra("MyUser.gender");

				storeUser(name, age, gender);
				
				startActivity(new Intent(LoginSuccessActivity.this, 
						MainActivity.class));
				finish();
			}
		});
	}

	protected void storeUser(String name, int age, Gender gender) {
		final MyPreferences prefs = MyPreferences.getInstance(getApplicationContext());

		if (Globals.SIMPLE_PREFERENCES) {
			prefs.setUserName(name);
			prefs.setAge(age);
			prefs.setGender(gender);
		} else {
			MyUser user = new MyUser();
			user.setUserName(name);
			user.setAge(age);
			user.setGender(gender);
			prefs.setUser(user);
		}
	}
}

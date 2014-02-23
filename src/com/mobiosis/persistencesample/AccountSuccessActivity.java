package com.mobiosis.persistencesample;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobiosis.persistencesample.data.SQLiteHelper;
import com.mobiosis.persistencesample.model.MyUser;

public class AccountSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login_success);
		
		findViewById(R.id.button_continue).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyUser user = getIntent().getParcelableExtra("MyUser");
				
				//MyUser user = (MyUser) getIntent().getSerializableExtra("MyUser");
				
				addUserToDB(user);
				
				Intent intent = new Intent(AccountSuccessActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

		});

	}
	
	private void addUserToDB(MyUser user) {
//		ContentResolver cr = getContentResolver();
//		
//		ContentValues values = new ContentValues();
//		values.put(MyUser.USER_NAME, user.getUserName());
//		values.put(MyUser.AGE, user.getAge());
//		values.put(MyUser.GENDER, user.getGender().ordinal());
//		
//		cr.insert(
//				Uri.withAppendedPath(SampleProvider.getUri(), SQLiteHelper.DatabaseType.USERS.name()), 
//				values);
		
		//using the DAO
		SQLiteHelper helper = SQLiteHelper.getInstance(getApplicationContext());
		try {
			helper.getUsers().create(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

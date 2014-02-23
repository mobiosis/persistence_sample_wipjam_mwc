package com.mobiosis.persistencesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.mobiosis.persistencesample.model.MyUser.Gender;

public class LoginActivity extends Activity {
	
	private EditText mEditName;
	private Button mButtonContinue;
	private Spinner mEditGender;
	private NumberPicker mEditAge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		initUI();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		String name = "";
		if (!TextUtils.isEmpty(mEditName.getText())) {
			name = mEditName.getText().toString();					
		}

		Gender gender = Gender.valueOf(mEditGender.getSelectedItem().toString());

		outState.putString("MyUser.name", name);
		outState.putInt("MyUser.gender_selection", mEditGender.getSelectedItemPosition());
		outState.putInt("MyUser.age", mEditAge.getValue());

	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			mEditName.setText(savedInstanceState.getString("MyUser.name"));
			mEditAge.setValue(savedInstanceState.getInt("MyUser.age"));
			mEditGender.setSelection(savedInstanceState.getInt("MyUser.gender_selection"));
		}
	}

	private void initUI() {
		mEditName = (EditText)findViewById(R.id.user_name);
		mEditGender = (Spinner)findViewById(R.id.gender);
		mEditAge = (NumberPicker)findViewById(R.id.age);
		mEditAge.setMinValue(1);
		mEditAge.setMaxValue(100);
		mButtonContinue = (Button)findViewById(R.id.button_continue);
		mButtonContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean error = false;

				String name = "";
				if (TextUtils.isEmpty(mEditName.getText())) {
					mEditName.setError("Name cannot be empty");
					error = true;
				} else {
					name = mEditName.getText().toString();					
				}
				
				Gender gender = Gender.valueOf(mEditGender.getSelectedItem().toString());
				
				int age = mEditAge.getValue();
				
				if (!error) {
					setupUser(name, age, gender);
				}
			}
		});
	}

	protected void setupUser(String name, int age, Gender gender) {
		Intent intent = new Intent(this, LoginSuccessActivity.class);
		intent.putExtra("MyUser.name", name);
		intent.putExtra("MyUser.gender", gender);
		intent.putExtra("MyUser.age", age);
				
		startActivity(intent);
		
		
//		//what if we wanted to store the whole object at once?
//		MyUser user = new MyUser();
//		user.setUserName(name);
//		user.setAge(age);
//		user.setGender(gender);
//		intent.putExtra("MyUser", user);
	}

}

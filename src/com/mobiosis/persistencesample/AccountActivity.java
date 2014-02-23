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

import com.mobiosis.persistencesample.model.MyUser;
import com.mobiosis.persistencesample.model.MyUser.Gender;

public class AccountActivity extends Activity {
	private EditText mEditName;
	private Button mButtonContinue;
	private Spinner mEditGender;
	private NumberPicker mEditAge;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		initUI();

		initFromBundle(savedInstanceState);
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
				
				MyUser.Gender gender = Gender.Male;
				if (TextUtils.isEmpty(mEditName.getText())) {
					mEditName.setError("Name cannot be empty");
					error = true;
				} else {
					name = mEditName.getText().toString();
				}
				
				String selectedGender = mEditGender.getSelectedItem().toString();
				if (selectedGender.equals(getString(R.string.gender_female))) {
					gender = Gender.Female;
				}
				
				int age = mEditAge.getValue();
				
				if (!error) {
					addUser(name, age, gender);
				}
			}
		});		
	}

	protected void addUser(String name, int age, Gender gender) {
		Intent intent = new Intent(this, AccountSuccessActivity.class);
		MyUser user = new MyUser(name, age, gender);
		intent.putExtra("MyUser", user);
		
//		ArrayList<MyUser> list = new ArrayList<MyUser>();
//		list.add(user);
//		intent.putParcelableArrayListExtra("MyUser[]", list);

		startActivity(intent);		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		String name = "";
		MyUser.Gender gender = Gender.Male;

		if (!TextUtils.isEmpty(mEditName.getText())) {
			name = mEditName.getText().toString();
		}
		
		String selectedGender = mEditGender.getSelectedItem().toString();
		if (selectedGender.equals(getString(R.string.gender_female))) {
			gender = Gender.Female;
		}
		
		int age = mEditAge.getValue();
		
		outState.putString("MyUser.name", name);
		outState.putInt("MyUser.age", age);
		outState.putSerializable("MyUser.gender", gender);
	}
	
	private void initFromBundle(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			String name = savedInstanceState.getString("MyUser.name");
			int age = savedInstanceState.getInt("MyUser.age");
			Gender gender = (Gender)savedInstanceState.getSerializable("MyUser.gender");
			
			mEditName.setText(name);
			mEditAge.setValue(age);
			mEditGender.setSelection(gender.ordinal());
		}
	}

}

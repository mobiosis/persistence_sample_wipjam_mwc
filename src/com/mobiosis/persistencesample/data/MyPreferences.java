package com.mobiosis.persistencesample.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mobiosis.persistencesample.model.MyUser;
import com.mobiosis.persistencesample.model.MyUser.Gender;
import com.google.gson.Gson;

public class MyPreferences {
	
	private static MyPreferences mInstance;
	private SharedPreferences mPreferences;
	
	private SharedPreferences.Editor mEditor;
	
	private MyPreferences() {};
	synchronized public static MyPreferences getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MyPreferences();
			mInstance.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			mInstance.mEditor = mInstance.mPreferences.edit();
		}
		return mInstance;
	}
	public String getUserName() {		
		return mPreferences.getString("MyUser.name", "");
	}
	public void setUserName(String userName) {
		mEditor.putString("MyUser.name", userName);
		mEditor.commit();
	}	
	
	public int getAge() {
		return mPreferences.getInt("MyUser.age", 0);
	}
	
	public void setAge(int age) {
		mEditor.putInt("MyUser.age", age);
		mEditor.commit();
	}
	
	public Gender getGender() {
		String genderName = mPreferences.getString("MyUser.gender", Gender.Male.name());
		if (Gender.Female.name().equals(genderName)) {
			return Gender.Female;
		}
		return Gender.Male;
	}
	
	public void setGender(Gender gender) {
		mEditor.putString("MyUser.gender", gender.name());
		mEditor.commit();
	}
	
	public MyUser getUser() {
		String userString = mPreferences.getString("MyUser", "");
		Gson gson = new Gson();
		MyUser user = gson.fromJson(userString, MyUser.class);
		return user;
	}

	public void setUser(MyUser user) {
		Gson gson = new Gson();
		String userString = gson.toJson(user);
		mEditor.putString("MyUser", userString);
		mEditor.commit();		
	}
}

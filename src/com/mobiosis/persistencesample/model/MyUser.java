package com.mobiosis.persistencesample.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public class MyUser implements Parcelable {
	
	public static final String USER_NAME = "user_name";
	public static final String AGE = "age";
	public static final String GENDER = "gender";
	public static final String COUNTRY = "country";
	
	public enum Gender {
		Male,
		Female
	};
	
	//@SerializedName("custom_user_name")
	@DatabaseField
	private String mUserName;
	@DatabaseField
	private int mAge;
	@DatabaseField
	private Gender mGender;

	//
	private ArrayList<String>mNicknames;
	
	public MyUser() {
		mUserName = "";		
		mNicknames = new ArrayList<String>();		
	}
	
	public MyUser(String name, int age, Gender gender) {
		mUserName = name;
		mAge = age;
		mGender = gender;
		mNicknames = new ArrayList<String>();
	}
	
	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public int getAge() {
		return mAge;
	}
	public void setAge(int mAge) {
		this.mAge = mAge;
	}
	public Gender getGender() {
		return mGender;
	}
	public void setGender(Gender mGender) {
		this.mGender = mGender;
	}

	public MyUser(Parcel in) {
		readFromParcel(in);
	}	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void readFromParcel(Parcel in) {
		mUserName = in.readString();
		mAge = in.readInt();
		mGender = (Gender) in.readSerializable();
		mNicknames = new ArrayList<String>();
		in.readStringList(mNicknames);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mUserName);
		dest.writeInt(mAge);
		dest.writeSerializable(mGender);
		dest.writeStringList(mNicknames);
	}
	
	public static final Parcelable.Creator<MyUser> CREATOR = new Parcelable.Creator<MyUser>() {
		public MyUser createFromParcel(Parcel in) {
			return new MyUser(in);
		}

		public MyUser[] newArray(int size) {
			return new MyUser[size];
		}
	};	
	

}

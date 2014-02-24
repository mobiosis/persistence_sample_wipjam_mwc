package com.mobiosis.persistencesample;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobiosis.persistencesample.data.Globals;
import com.mobiosis.persistencesample.data.MyPreferences;
import com.mobiosis.persistencesample.data.SQLiteHelper;
import com.mobiosis.persistencesample.model.MyUser;

public class MainActivity extends FragmentActivity 
//implements LoaderCallbacks<Cursor>
implements LoaderCallbacks<List<MyUser>>
{

	private MyUser mUser;
	private MyPreferences mPreferences;
	private SimpleCursorAdapter mCursorAdapter;
	private ArrayAdapter<MyUser> mListAdapter;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPreferences = MyPreferences.getInstance(getApplicationContext());
		
		if (!isUserSet()) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}
		
		setContentView(R.layout.activity_main);
        mList = (ListView)findViewById(R.id.list);
        
        if (savedInstanceState != null) {
			Log.d(MainActivity.class.getName(), "onRestore");
			mList.onRestoreInstanceState(savedInstanceState.getParcelable("mList"));
			Log.d(MainActivity.class.getName(), "restored");
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		initUser();
		
		initList();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("mList", mList.onSaveInstanceState());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}	

	private boolean isUserSet() {
		if (Globals.SIMPLE_PREFERENCES) return !TextUtils.isEmpty(mPreferences.getUserName());
		else return !TextUtils.isEmpty(mPreferences.getUser().getUserName());
	}
	
	private void initUser() {
		if (Globals.SIMPLE_PREFERENCES) {
			mUser = new MyUser();
			mUser.setUserName(mPreferences.getUserName());
			mUser.setAge(mPreferences.getAge());
			mUser.setGender(mPreferences.getGender());
		} else {
			mUser = mPreferences.getUser();
		}
		((TextView)findViewById(R.id.hello)).setText(getString(R.string.hello_world, mUser.getUserName()));
	}
	
	private void initList() {
        // Create an empty adapter we will use to display the loaded data.
        mCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[] { MyUser.USER_NAME, MyUser.AGE },
                new int[] { android.R.id.text1, android.R.id.text2}, 0);
        
        mListAdapter = new ArrayAdapter<MyUser>(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text1) {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		View v = super.getView(position, convertView, parent);
        		((TextView)v.findViewById(android.R.id.text1)).setText(getItem(position).getUserName());
        		((TextView)v.findViewById(android.R.id.text2)).setText(
        				getItem(position).getAge() + ", " + getItem(position).getGender().name() 
//        				+ ", " + getItem(position).getCountry()
        				);
        		return v;
        	}
        };

        mList.setAdapter(mListAdapter);
        
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			startActivity(new Intent(this, AccountActivity.class));
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

//	@Override
//	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
//		
//		return new CursorLoader(this,
//				Uri.withAppendedPath(SampleProvider.getUri(), SQLiteHelper.DatabaseType.USERS.name()),
//				SQLiteHelper.sUsersColumns.keySet().toArray(new String[]{}), 
//				null, null,null);
//	}
//	
//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//		mAdapter.changeCursor(cursor);
//		mAdapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		// TODO Auto-generated method stub
//		
//	}
	
	public Loader<List<MyUser>> onCreateLoader(int arg0, Bundle arg1) {
		return new AsyncTaskLoader<List<MyUser>>(this) {
			
			@Override
			protected void onStartLoading() {
				super.onStartLoading();
				
				forceLoad();
			}
			
			@Override
			protected List<MyUser> onLoadInBackground() {
				return loadInBackground();
			}

			@Override
			public List<MyUser> loadInBackground() {
				//using the DAO
				SQLiteHelper helper = SQLiteHelper.getInstance(getApplicationContext());
				try {
					return helper.getUsers().queryForAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<MyUser>> loader, List<MyUser> list) {
		mListAdapter.clear();
		if (list != null && list.size() > 0) {
			mListAdapter.addAll(list);
			mListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<MyUser>> arg0) {
		// TODO Auto-generated method stub
	}

}

package com.mobiosis.persistencesample;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
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
import com.mobiosis.persistencesample.data.SampleProvider;
import com.mobiosis.persistencesample.model.MyUser;

public class MainActivity extends FragmentActivity 
implements LoaderCallbacks<Cursor>
{

	private MyUser mUser;
	private MyPreferences mPreferences;
	private SimpleCursorAdapter mAdapter;
	private ArrayAdapter<MyUser> mListAdapter;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPreferences = MyPreferences.getInstance(getApplicationContext());
		
		if (!isUserSet()) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		initUser();
		
		initList();		

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
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[] { MyUser.USER_NAME, MyUser.AGE },
                new int[] { android.R.id.text1, android.R.id.text2}, 0);
        
        mList = (ListView)findViewById(R.id.list);
        mList.setAdapter(mAdapter);
        
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

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		return new CursorLoader(this,
				Uri.withAppendedPath(SampleProvider.getUri(), SQLiteHelper.DatabaseType.USERS.name()),
				SQLiteHelper.sUsersColumns.keySet().toArray(new String[]{}), 
				null, null,null);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}


}

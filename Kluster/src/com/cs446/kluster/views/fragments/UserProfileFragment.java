package com.cs446.kluster.views.fragments;

import android.app.Activity;
import android.app.Fragment;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.cs446.kluster.R;
import com.cs446.kluster.data.PhotoProvider;
import com.cs446.kluster.net.AuthKlusterRestAdapter;
import com.cs446.kluster.net.KlusterService;
import com.cs446.kluster.net.PhotosCallback;

public class UserProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	PhotoGridAdapter mAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.userprofile_page , container, false);
		
		String[] cols = new String[] { "location" };
		int[]   views = new int[]   { R.id.photogrid_imgBackground };
		
		mAdapter = new PhotoGridAdapter(getActivity(), R.layout.photogridcell_layout, null, cols, views, 0);
		
		GridView gridView=(GridView)view.findViewById(R.id.profilePagePhotoGrid);
		gridView.setAdapter(mAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PhotoViewerFragment fragment = new PhotoViewerFragment();		
				Bundle bundle = new Bundle();
				bundle.putString("url", (String)parent.getItemAtPosition(position));
				fragment.setArguments(bundle);
				
				getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack(fragment.toString()).commit();
			}
		});
		
	    /* Start loader */  
        getLoaderManager().initLoader(0, null, this);  
		
		RestAdapter restAdapter = new AuthKlusterRestAdapter().build();	
		KlusterService service = restAdapter.create(KlusterService.class);	
		Log.w("UserProfileFragment", "UserId: "+ getArguments().getString("userid"));
		service.getPhotosByUserIds(getArguments().getString("userid"), new PhotosCallback(getActivity()));		
		getActivity().getActionBar().setTitle(getArguments().getString("username"));
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity().getFragmentManager().getBackStackEntryCount() > 0) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        String[] selectionArgs = {getArguments().getString("userid")};
		return new CursorLoader(getActivity(), PhotoProvider.CONTENT_URI, null, "userid = ?", selectionArgs, null);
	}
		
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		Log.w("UserProfileFragment", Integer.toString(mAdapter.getCount()));
		mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
}

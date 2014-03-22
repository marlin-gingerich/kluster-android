package com.cs446.kluster.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cs446.kluster.R;
import com.cs446.kluster.photo.PhotoProvider;

public class EventGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	
	EventGridAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.eventgrid_layout, container, false);
		
		String[] cols = new String[] { "location" };
		int[]   views = new int[]   { R.id.eventgrid_txtTitle };
		
		adapter = new EventGridAdapter(getActivity(), R.layout.eventgridcell_layout, null, cols, views, 0);
		
		GridView gridView=(GridView)view.findViewById(R.id.eventGrid);
		gridView.setAdapter(adapter);
        
        /* Start loader */  
        getLoaderManager().initLoader(0, null, this);   
        
        setHasOptionsMenu(true);
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
		return new CursorLoader(getActivity(), PhotoProvider.CONTENT_URI, null, null, null, null);
	}
		
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.changeCursor(null);
	}
}
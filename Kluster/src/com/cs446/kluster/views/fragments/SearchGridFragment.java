package com.cs446.kluster.views.fragments;

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
import com.cs446.kluster.data.EventProvider;
import com.cs446.kluster.data.EventStorageAdapter;
import com.cs446.kluster.data.serialize.EventSerializer;
import com.cs446.kluster.models.Event;
import com.cs446.kluster.net.EventRequest;
import com.cs446.kluster.net.http.task.HttpContentRequestTask;

public class SearchGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private EventGridAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.searchgrid_layout, container, false);
		
		String[] cols = new String[] { "location" };
		int[]   views = new int[]   { R.id.eventgrid_txtTitle };
		
		mAdapter = new EventGridAdapter(getActivity(), R.layout.eventgridcell_layout, null, cols, views, 0);
		
		GridView gridView=(GridView)view.findViewById(android.R.id.list);
		gridView.setAdapter(mAdapter);

        /* Start loader */  
        getLoaderManager().initLoader(0, null, this);   
        
        setHasOptionsMenu(true);
        getActivity().getActionBar().setTitle("Discover");
        
		return view;
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity().getFragmentManager().getBackStackEntryCount() > 0) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		Bundle args = getArguments();
		double radius = 30000;
		
		HttpContentRequestTask<Event> task = new HttpContentRequestTask<Event>(new EventSerializer(), new EventStorageAdapter(getActivity().getContentResolver()));
		EventRequest request = EventRequest.create(args.getString("location"), radius);
		
		task.executeAsync(request);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		Bundle args = getArguments();
		
		String[] selection = new String[] {args.getString("location")};
		
		return new CursorLoader(getActivity(), EventProvider.CONTENT_URI, null, "location = ?", selection, null);
	}
		
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.changeCursor(null);
	}
}

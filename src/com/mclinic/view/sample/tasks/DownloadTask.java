package com.mclinic.view.sample.tasks;

import android.os.AsyncTask;

import com.burkeware.search.api.Context;
import com.mclinic.api.service.AdministrativeService;
import com.mclinic.util.Constants;
import com.mclinic.view.sample.listeners.DownloadListener;

public abstract class DownloadTask extends AsyncTask<String, String, String> {

	protected DownloadListener mStateListener;
	
	protected String executeResolver(String... values) {
		String resolver = values[0]; 
		AdministrativeService adminService = Context.getInstance(AdministrativeService.class);
		if (resolver.equals(Constants.COHORT))
			adminService.downloadCohorts();
		if (resolver.equals(Constants.PATIENT))
			adminService.downloadCohortPatients(values[1]);
		
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		synchronized (this) {
			if (mStateListener != null) {
				// update progress and total
				if (values.length > 1)
					mStateListener.progressUpdate(values[0], Integer.valueOf(values[1]), Integer.valueOf(values[2]));
				else
					mStateListener.progressUpdate(values[0]);
			}
		}
	}

	@Override
	protected void onPostExecute(String result) {
		synchronized (this) {
			if (mStateListener != null) {
				mStateListener.taskComplete(result);
			}
		}
	}

	public void setDownloadListener(DownloadListener dl) {
		synchronized (this) {
			mStateListener = dl;
		}
	}
}
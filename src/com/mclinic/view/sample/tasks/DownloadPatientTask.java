package com.mclinic.view.sample.tasks;

import com.mclinic.util.Constants;
import com.mclinic.view.sample.listeners.DownloadListener;

public class DownloadPatientTask extends DownloadTask {

	protected DownloadListener mStateListener;
	
	@Override
	protected String doInBackground(String... values) {
		executeResolver(Constants.PATIENT, values[0]);
		return null;
	}
}
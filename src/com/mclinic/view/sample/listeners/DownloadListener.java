package com.mclinic.view.sample.listeners;

import java.util.HashMap;

public interface DownloadListener {
	void taskComplete(String result);
	void taskComplete(HashMap<String, Object> result);
	void progressUpdate(String message, int progress, int max);
	void progressUpdate(String progress);
}

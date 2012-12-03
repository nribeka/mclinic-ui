package com.mclinic.view.sample.utilities;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

public class App extends Application {

	private Handler mHandler;

	private static App mSingleton = null;

	private Runnable mRunnable;
	// how long to sleep the timer
	private long mTimerDelay = 5 * 60 * 1000; // wake up every five minutes
	private long mTimerTimeout = 15 * 60 * 1000; // timeout fifteen minutes

	private long mElapsedTime = 0;

	private long mTouchedTime = System.currentTimeMillis();
	private boolean isAppLocked = true;
	private boolean isTimerEnabled = true;

	public boolean isLocked() {
		return isAppLocked;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mSingleton = this;
		mHandler = new Handler();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (isTimerEnabled) {
					mElapsedTime = System.currentTimeMillis() - mTouchedTime;
				} else {
					mElapsedTime = 0;
					mTouchedTime = System.currentTimeMillis();
				}
				if (isTimerEnabled && (mElapsedTime > mTimerTimeout)) {
					setAppLocked(true);
					sendBroadcast(new Intent(Constants.SCREEN_LOCKER));
				}
				mHandler.postDelayed(this, mTimerDelay);
			}
		};
		mHandler.postDelayed(mRunnable, mTimerDelay);

	}

	public void onUserInteraction() {
		mTouchedTime = System.currentTimeMillis();
	}

	public void setAppLocked(boolean appLocked) {
		this.isAppLocked = appLocked;
		this.isTimerEnabled = !appLocked;
	}

	public static App getApp() {
		return mSingleton;
	}
}

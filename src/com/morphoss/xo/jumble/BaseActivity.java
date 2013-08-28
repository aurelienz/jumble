package com.morphoss.xo.jumble;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	protected static JumbleApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (JumbleApplication) getApplication();
	}
}

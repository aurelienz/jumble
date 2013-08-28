package com.morphoss.xo.jumble.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.morphoss.xo.jumble.R;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/credits.html");
	}
}
package com.cyl.simpleprogressbar;

import com.example.simpleprogressbar.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private LinearLayout mRoot;
	private int progress = 0;
	private ColorProgressBar mProgresses[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		start();
	}

	private void start() {
		new Thread() {
			public void run() {
				while (progress != 100) {
					progress++;
					SystemClock.sleep(200);
					for (int i = 0; i < mRoot.getChildCount(); i++) {
						mProgresses[i].setProgress(progress);
					}
				}
			};
		}.start();
	}

	private void findView() {
		mRoot = (LinearLayout) findViewById(R.id.root);
		mProgresses = new ColorProgressBar[mRoot.getChildCount()];
		for (int i = 0; i < mRoot.getChildCount(); i++) {
			mProgresses[i] = (ColorProgressBar) mRoot.getChildAt(i);
		}
	}
}

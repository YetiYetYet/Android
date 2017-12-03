package com.example.marjorie.color_match;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class ColorMatchGame extends AppCompatActivity implements Runnable {

	private GameView mGameView;

	private long timerFromResume;
	private Handler timerHandler;
	private long timerTotal = 0;
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timerHandler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		timerFromResume = System.currentTimeMillis();
		thread = new Thread(this);
		thread.start();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.game);
		mGameView = (GameView)findViewById(R.id.GameView);
		mGameView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.timerTotal += (System.currentTimeMillis() - timerFromResume);
		Log.e(getClass().getSimpleName(), "PAUSED");
		this.timerHandler.removeCallbacks(this);
	}

	@Override
	public void run() {
		final long currentTime = this.getCurrentTime();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e(getClass().getSimpleName(), "running" + String.valueOf(currentTime));
			}
		});
		this.timerHandler.postDelayed(this, 1000);
	}

	private long getCurrentTime(){
		long delay = (System.currentTimeMillis() - this.timerFromResume) + this.timerTotal;
		return delay / 1000;
	}
}

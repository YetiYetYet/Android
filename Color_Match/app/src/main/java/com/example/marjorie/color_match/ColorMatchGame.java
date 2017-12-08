package com.example.marjorie.color_match;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ColorMatchGame extends AppCompatActivity implements Runnable {

	private GameView mGameView;

	private long timerFromResume;
	private Handler timerHandler;
	private long timerTotal = 0;
	private Thread thread;

	private double current_time = 0.;

	public double getCurrent_time() {
		return current_time;
	}

	private double max_time = 136000.;



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

		mGameView.setEventListener(new GameView.IMyEventListener() {

			@Override
			public void onEventAccured() {
				Log.e("UGH", "They touch children");
				//mMenuView.setVisibility(View.INVISIBLE);
				if(mGameView.buttonPressed == 2){
					DataBase db = new DataBase(ColorMatchGame.this);
					db.insert(mGameView.score, mGameView.actualString);
				}
				finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.timerTotal += (System.currentTimeMillis() - timerFromResume);
		Log.e(getClass().getSimpleName(), "PAUSED");
		this.timerHandler.removeCallbacks(this);
	}

	private int niveau_;
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("niveau", niveau_);
		//outState.putInt("niveau", mGameView.getNiveau());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//mGameView.init(savedInstanceState.getInt("niveau"));
	}


	@Override
	public void run() {
		final long currentTime = (System.currentTimeMillis() - this.timerFromResume) + this.timerTotal;
		current_time = 1 - (currentTime / max_time);
		//Log.e(getClass().getSimpleName(), String.valueOf(current_time));
		/*
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.e(getClass().getSimpleName(), "running" + String.valueOf(currentTime));
			}
		});
		*/
		this.timerHandler.postDelayed(this, 100);
	}
}

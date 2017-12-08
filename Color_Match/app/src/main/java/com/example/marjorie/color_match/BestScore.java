package com.example.marjorie.color_match;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by marjorie on 08/12/17.
 */

public class BestScore extends AppCompatActivity {
	private BestScoreView mBestScoreView;
	public String[] currentScore;

	public String[] getcurrentScore() {
		return currentScore;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bestscore);
		DataBase db = new DataBase(this);
		mBestScoreView = (BestScoreView)findViewById(R.id.BestScoreView);
		mBestScoreView.setVisibility(View.VISIBLE);
		currentScore = db.StringSelectAll();

	}
}

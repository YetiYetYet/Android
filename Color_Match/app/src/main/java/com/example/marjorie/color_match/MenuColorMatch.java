package com.example.marjorie.color_match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuColorMatch extends AppCompatActivity {

	private MenuView mMenuView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		mMenuView = (MenuView)findViewById(R.id.MenuView);
		mMenuView.setVisibility(View.VISIBLE);

		mMenuView.setEventListener(new MenuView.IMyEventListener() {

			@Override
			public void onEventAccured() {
				Log.e("UGH", "They touch children");
				//mMenuView.setVisibility(View.INVISIBLE);
				if(mMenuView.buttonPressed == 0)
					startGame();
				if(mMenuView.buttonPressed == 1)
					startBestScore();
				if(mMenuView.buttonPressed == 2)
					startCredit();
				if(mMenuView.buttonPressed == 3)
					finish();
			}
		});
	}

	public void startGame() {
		Intent intent = new Intent(this, ColorMatchGame.class);
		startActivity(intent);
	}

	public void startBestScore() {
		Intent intent = new Intent(this, BestScore.class);
		startActivity(intent);
	}

	public void startCredit() {
		Intent intent = new Intent(this, Credit.class);
		startActivity(intent);
	}

}


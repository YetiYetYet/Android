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
				Log.e("-OTE-", "They touch my children");
				//mMenuView.setVisibility(View.INVISIBLE);
				startGame();
			}
		});
	}

	public void startGame() {
		Intent intent = new Intent(this, ColorMatchGame.class);
		startActivity(intent);
	}

}


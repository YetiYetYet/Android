package com.example.marjorie.colormatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;


public class ColorMatchGame extends AppCompatActivity {

	private MenuView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		Intent intent = getIntent();
		mGameView = (MenuView)findViewById(R.id.GameView);
		mGameView.setVisibility(View.VISIBLE);
	}

}

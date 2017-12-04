package com.example.marjorie.color_match;

/**
 * Created by marjorie on 20/11/17.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private Resources 	mRes;
	private Context 	mContext;
	private     Thread  cv_thread;
	Paint paint;
	SurfaceHolder holder;
	volatile private boolean in = true;


	/**
	 * The constructor called from the main JetBoy activity
	 *
	 * @param context
	 * @param attrs
	 */

	int [][] board = new int[10][14];
	int temp [][] = new int[4][3];
	private Bitmap soul7;
	private Bitmap soul1;
	private Bitmap soul2;
	private Bitmap soul3;
	private Bitmap soul4;
	private Bitmap soul5;
	private Bitmap soul6;
	private Bitmap background;
	private int actualBackGround;
	private double life;
	private int score;
	private int scoreofthismove;
	private int NbOcc;
	private int actualOcc[] = new int[2];
	MediaPlayer Mmusic=new MediaPlayer();
	MediaPlayer Mfirstsong=new MediaPlayer();
	MediaPlayer yeah=new MediaPlayer();
	private boolean BfirstSong;
	private int HearthBeatTime;
	private int HearthBeat;


	public GameView(Context context, AttributeSet attrs){
		super(context, attrs);

		mContext	= context;
		mRes 		= mContext.getResources();


		// permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
		holder = getHolder();
		holder.addCallback(this);

		soul1 = BitmapFactory.decodeResource(mRes, R.drawable.soul1);
		soul2 = BitmapFactory.decodeResource(mRes, R.drawable.soul2);
		soul3 = BitmapFactory.decodeResource(mRes, R.drawable.soul3);
		soul4 = BitmapFactory.decodeResource(mRes, R.drawable.soul4);
		soul5 = BitmapFactory.decodeResource(mRes, R.drawable.soul5);
		soul6 = BitmapFactory.decodeResource(mRes, R.drawable.soul6);
		soul7 = BitmapFactory.decodeResource(mRes, R.drawable.soul7);

		Mmusic = MediaPlayer.create(mContext, R.raw.game);
		Mmusic.setLooping(true);
		Mfirstsong = MediaPlayer.create(mContext, R.raw.hoyes);
		yeah = MediaPlayer.create(mContext, R.raw.yeah);
		yeah.setVolume(0.80f, 0.80f);

		cv_thread   = new Thread(this);
		initparameters();
	}


	// callback sur le cycle de vie de la surfaceview
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i("-> FCTG <-", "surfaceChanged "+ width +" - "+ height);
		in = true;
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("-> FCTG <-", "surfaceCreated");
		in = true;
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("-> FCTG <-", "surfaceDestroyed");
		Mmusic.pause();
		in = false;

	}

	private void nDraw(Canvas canvas) {
		if(canvas == null)
			return;
		canvas.drawRGB(0,0,0);

		if (actualBackGround > 1097) {
			actualBackGround = 0;
		}
		actualBackGround += 1;

		if((HearthBeatTime > 0 && HearthBeatTime <= 2) || (HearthBeatTime > 4 && HearthBeatTime <= 6)){
			HearthBeat = 5;
		}else
			HearthBeat = 0;

		HearthBeatTime++;
		if(HearthBeatTime > 12)
			HearthBeatTime = 0;

		InputStream assetInStream=null;
		try {
			assetInStream= mContext.getAssets().open("frame"+actualBackGround+".png");
			Bitmap acc = BitmapFactory.decodeStream(assetInStream);
			background = Bitmap.createScaledBitmap(acc, acc.getWidth()*4, acc.getHeight()*4, false);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(assetInStream!=null)
				System.out.println();
		}

		Paint myPaint = new Paint();
		myPaint.setColor(Color.rgb(255, 0, 0));
		canvas.drawBitmap(background, -20, 200, null);
		canvas.drawRect(20, 20, getWidth()-20, 160, myPaint);
		myPaint.setColor(Color.rgb(255, 255, 0));
		canvas.drawRect(20, 20, (getWidth()-20)*(float)life, 160, myPaint);
		myPaint.setColor(Color.rgb(0, 0, 0));
		canvas.drawRect(0, 200, 150, 530, myPaint);
		drawSoul(canvas);

		paint.setColor(Color.rgb(255, 255, 255));
		if(score<1000)
			paint.setTextSize(100);
		if(score>1000)
			paint.setTextSize(94);
		canvas.drawText("You have : "+  Integer.toString(score) + "pt Darling", 0, getHeight()-50, paint);
		life = ((ColorMatchGame)getContext()).getCurrent_time();
		//life += -0.002;
	}

	public void drawSoul(Canvas c){
		int i, j;
		for(i = 0; i < 10; i++){
			for(j = 0; j < 14; j++){
				switch (board[i][j]){
					case 1:
						soul1 = Bitmap.createScaledBitmap(soul1, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul1, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 2:
						soul2 = Bitmap.createScaledBitmap(soul2, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul2, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 3:
						soul3 = Bitmap.createScaledBitmap(soul3, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul3, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 4:
						soul4 = Bitmap.createScaledBitmap(soul4, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul4, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 5:
						soul5 = Bitmap.createScaledBitmap(soul5, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul5, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 6:
						soul6 = Bitmap.createScaledBitmap(soul6, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul6, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 7:
						soul7 = Bitmap.createScaledBitmap(soul7, getWidth()/10+HearthBeat, (getHeight()-200)/14+HearthBeat, false);
						c.drawBitmap(soul7, i*(getWidth()/10)-(HearthBeat/2), j*((getHeight()-350)/14)+200-(HearthBeat/2), null);
						break;
					case 0:
						break;
					default:
						InputStream assetInStream=null;
						try {
							assetInStream= mContext.getAssets().open("frame"+board[i][j]+".png");
							Bitmap acc = BitmapFactory.decodeStream(assetInStream);
							acc = Bitmap.createScaledBitmap(acc, acc.getWidth()/2, acc.getHeight()/2, false);
							c.drawBitmap(acc, (i*(getWidth()/10)), (j*((getHeight()-350)/14)+200), null);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if(assetInStream!=null)
								System.out.println();
						}
						if(board[i][j] == -31)
							board[i][j]=0;
						else
							board[i][j]--;
						break;
				}
 			}
		}
	}

	public boolean validMoveAndCoord (int x, int y){
		for(int i = 0; i<4; i++){
			for(int j = 0; j<3; j++){
				temp[i][j] = 0-1-i;
			}
		}
		int tempx = x;
		int tempy = y;
		if(tempx > 9 || tempy > 13 || tempx < 0 || tempy < 0){
			return false;
		}
		if(board[tempx][tempy] > 0 && board[tempx][tempy] < 8){
			return false;
		}
		if(x != 0){
			while (tempx > 0){
				tempx -= 1;
				if(board[tempx][tempy] != 0 && board[tempx][tempy] != 8){
					Log.e("-Gauche", "GAUCHE");
					temp[0][0] = board[tempx][tempy];
					temp[0][1] = tempx;
					temp[0][2] = tempy;
					break;
				}
			}
		}
		tempx = x;
		if(x != 9){
			while (tempx < 9){
				tempx += 1;
				if(board[tempx][tempy] != 0 && board[tempx][tempy] != 8){
					Log.e("DROITE", "DROITE");
					temp[1][0] = board[tempx][tempy];
					temp[1][1] = tempx;
					temp[1][2] = tempy;
					break;
				}
			}
		}
		tempx = x;
		if(y != 0){
			while (tempy > 0){
				tempy -= 1;
				if(board[tempx][tempy] != 0 && board[tempx][tempy] != 8){
					Log.e("-BAS", "BAS");
					temp[2][0] = board[tempx][tempy];
					temp[2][1] = tempx;
					temp[2][2] = tempy;
					break;
				}
			}
		}
		tempy = y;
		if(y != 13){
			while (tempy < 13){
				tempy += 1;
				if(board[tempx][tempy] != 0 && board[tempx][tempy] != 8){
					Log.e("HAUT", "HAUT");
					temp[3][0] = board[tempx][tempy];
					temp[3][1] = tempx;
					temp[3][2] = tempy;
					break;
				}
			}
		}
		int color[] = new int[7];
		for(int i = 0; i<4; i++){
			if(temp[i][0] > 0){
				color[temp[i][0]-1]++;
			}
		}
		NbOcc = 0;
		for(int i = 0; i<7; i++){
			Log.e("-> RUN <-", "PB DANS RUN : " + i + color[i]);
			if(color[i]>1){
				if(color[i]==2){
					scoreofthismove = 40;
				}
				else{
					scoreofthismove = 60;
				}
				actualOcc[NbOcc] = i;
				NbOcc ++;
			}
		}
		if(NbOcc == 2)
			scoreofthismove = 120;
		if (NbOcc == 0){
			return false;
		}
		return true;
	}

	public void destroyIt(){
		for(int i = 0; i<NbOcc; i++){
			for(int j = 0;j<4;j++){
				Log.e("-> RUN <-", "?" + NbOcc +" " + actualOcc[i] +" "+(temp[j][0]-1));
				if(actualOcc[i] == (temp[j][0]-1)){
					Log.e("-> RUN <-", "I Enter it");
					board[temp[j][1]][temp[j][2]] = -1;
				}
			}
		}
	}

	public boolean nextPossibleMove(){
		int i;
		int j;
		for(i = 0; i < 10; i++){
			for(j = 0; j < 14; j++){
				if(validMoveAndCoord(i, j))
					return true;
			}
		}
		return false;
	}

	public void run(){
		Canvas c = null;
		while(in){
			try {
				cv_thread.sleep(01);
				try {
					if(BfirstSong && Mfirstsong.isPlaying() && !Mmusic.isPlaying()){
						Mmusic.start();
					}
					c = holder.lockCanvas(null);
					nDraw(c);
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);
					}
				}
			} catch(Exception e) {
				Log.e("-> RUN <-", "PB DANS RUN : " + e.getMessage());
			}
		}
	}

	public void initparameters(){
		if (!(paint != null))
			paint = new Paint();
		paint.setColor(0xFF0000);

		paint.setDither(true);
		paint.setColor(0xFFFFFF);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(3);
		paint.setTextAlign(Paint.Align.LEFT);
		actualBackGround = 0;
		life = 1;
		HearthBeatTime = 20;
		HearthBeat = 1;
		initBoard();
		Mfirstsong.start();
		BfirstSong = true;
		if ((cv_thread!=null) && (!cv_thread.isAlive())) {
			cv_thread = new Thread(this);
			cv_thread.start();
			Log.e("-FCT-", "cv_thread.start()");
			return;
		}
	}

	public void initBoard(){
		Random r = new Random();
		int nbr = r.nextInt(7);
		int i;
		int j;
		for(i = 0; i < 10; i++){
			for(j = 0; j < 14; j++){
				board[i][j] = nbr;
				nbr = r.nextInt(8);
				System.out.println(i+" "+j+" "+board[i][j]);
			}
		}
	}

	public boolean onTouchEvent (MotionEvent event) {
		Log.i("-> FCT <-", "onTouchEvent: "+ event.getX());
		Log.i("-> FCT <-", "onTouchEvent: "+ event.getY());
		int x = (int)event.getX()/(getWidth()/10);
		int y = (int)(event.getY()-200)/((getHeight()-350)/14);
		Log.i("-> FCT <-", "onTouchEvent: "+ x);
		Log.i("-> FCT <-", "onTouchEvent: "+ y);
		Log.i("-> FCT <-", "correct ?: "+ validMoveAndCoord(x, y));
		if(validMoveAndCoord(x, y)){
			destroyIt();
			yeah.start();
			score += scoreofthismove;
			Log.i("-> FCT <-", Boolean.toString(nextPossibleMove()));
		}
		return super.onTouchEvent(event);
	}
}

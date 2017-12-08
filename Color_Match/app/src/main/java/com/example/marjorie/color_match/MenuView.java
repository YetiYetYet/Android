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

public class MenuView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private Resources 	mRes;
	private Context 	mContext;

	private     Thread  cv_thread;

	Paint paint;
	SurfaceHolder holder;
	volatile private boolean in = true;
	MediaPlayer Mmusic=new MediaPlayer();
	MediaPlayer Mtransforms=new MediaPlayer();
	private Bitmap background;
	private int actualBackGround;
	private Bitmap button1;
	private Bitmap button2;
	private Bitmap button4;
	private Bitmap mute;
	private Bitmap mute2;
	private Bitmap unmute;
	private Bitmap unmute2;
	private boolean bmute = false;
	private boolean transform = false;
	private float white = 0;
	public int buttonPressed = 0;


	/**
	 * The constructor called from the main JetBoy activity
	 *
	 * @param context
	 * @param attrs
	 */


	public MenuView(Context context, AttributeSet attrs){
		super(context, attrs);

		mContext	= context;
		mRes 		= mContext.getResources();

		// permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
		holder = getHolder();
		holder.addCallback(this);

		button1 = BitmapFactory.decodeResource(mRes, R.drawable.button1);
		button2 = BitmapFactory.decodeResource(mRes, R.drawable.button2);
		button4 = BitmapFactory.decodeResource(mRes, R.drawable.button4);

		mute = BitmapFactory.decodeResource(mRes, R.drawable.sound1);
		unmute = BitmapFactory.decodeResource(mRes, R.drawable.sound2);
		unmute2 = Bitmap.createScaledBitmap(unmute, unmute.getWidth() / 11, unmute.getHeight() / 11, false);
		mute2 = Bitmap.createScaledBitmap(mute, mute.getWidth() / 11, mute.getHeight() / 11, false);

		Mmusic = MediaPlayer.create(mContext, R.raw.menu);
		Mtransforms = MediaPlayer.create(mContext, R.raw.transform);
		Mmusic.setLooping(true);

		cv_thread   = new Thread(this);

	}

	public interface IMyEventListener {
		public void onEventAccured();
	}

	private IMyEventListener mEventListener;

	public void setEventListener(IMyEventListener mEventListener) {
		this.mEventListener = mEventListener;
	}

	// callback sur le cycle de vie de la surfaceview
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		in = true;
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		in = true;
		initparameters();
		Mmusic = MediaPlayer.create(mContext, R.raw.menu);
		Mmusic.setLooping(true);
		if(!bmute)
			Mmusic.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Mmusic.pause();
		in = false;

	}

	private void nDraw(Canvas canvas) {
		if(canvas == null)
			return;
		canvas.drawRGB(0,0,0);
		if (actualBackGround < -20 && !transform) {
			actualBackGround = 0;
		}
		if(!transform)
			actualBackGround -= 1;
		if (actualBackGround > 8 && transform) {
			actualBackGround = 0;
		}
		if(transform)
			actualBackGround += 1;

		InputStream assetInStream=null;
		try {
			assetInStream= mContext.getAssets().open("mframe"+actualBackGround+".png");
			Bitmap acc = BitmapFactory.decodeStream(assetInStream);
			background = Bitmap.createScaledBitmap(acc, acc.getWidth()*4, acc.getHeight()*4, false);
			canvas.drawBitmap(background, 100, 200, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(assetInStream!=null)
				System.out.println();
		}
		if(transform && Mtransforms.isPlaying()) {
			canvas.drawARGB((int)white, 255, 255, 255);
			if(white<255)
				white += 3;
		}
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 165, 0));
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.STROKE);
		if(!transform) {
			if (bmute) {
				canvas.drawBitmap(unmute2, getWidth() - (10 + unmute2.getWidth()), 10, null);
			} else {
				canvas.drawBitmap(mute2, getWidth() - (10 + mute2.getWidth()), 10, null);
			}
			canvas.drawBitmap(button4, getWidth() / 2 - button1.getWidth() / 2, background.getWidth() + 400 + 40, null);
			canvas.drawBitmap(button1, getWidth() / 2 - button1.getWidth() / 2, background.getWidth() + 400 + 40 + (button1.getHeight() + 40), null);
			canvas.drawBitmap(button2, getWidth() / 2 - button1.getWidth() / 2, background.getWidth() + 400 + 40 + (button1.getHeight() + 40) * 2, null);
		}
	}

	public void run(){
		Canvas c = null;
		while(in){
			try {
				cv_thread.sleep(40);
				try {
					c = holder.lockCanvas(null);
					nDraw(c);
					if (mEventListener != null && !Mtransforms.isPlaying() && transform) {
						mEventListener.onEventAccured();
						transform = false;
						cv_thread.sleep(1500);
						white = 0;
					}
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
		if ((cv_thread!=null) && (!cv_thread.isAlive())) {
			cv_thread = new Thread(this);
			cv_thread.start();
			return;
		}
	}

	// fonction permettant de recuperer les evenements tactiles
	public boolean onTouchEvent (MotionEvent event) {

		if (event.getX() > getWidth() - (unmute2.getWidth() + 10) && event.getY() < (unmute2.getHeight() + 10)) {
			bmute = !bmute;
			if (bmute == true) {
				Mmusic.setVolume(0, 0);
			} else {
				Mmusic.setVolume(1, 1);
			}
			return super.onTouchEvent(event);
		}

		if (event.getY() > 200 && event.getY() < background.getHeight() + 200) {
			transform = true;
			Mmusic.stop();
			if (bmute)
				Mtransforms.setVolume(0, 0);
			else
				Mtransforms.setVolume(1, 1);
			Mtransforms.start();
			buttonPressed = 0;
			return super.onTouchEvent(event);
		}
		if(!transform) {
			if (event.getX() > getWidth() / 2 - button1.getWidth() / 2 && event.getX() < (getWidth() / 2 - button1.getWidth()) + button1.getWidth() * 1.5) {
				if (event.getY() > 400 + background.getHeight() + 40 && event.getY() < (440 + background.getHeight()) + button1.getHeight()) {
					buttonPressed = 1;
					mEventListener.onEventAccured();
					return super.onTouchEvent(event);
				}
				if (event.getY() > (480 + background.getHeight()) + button1.getHeight() && event.getY() < (440 + background.getHeight()) + (button1.getHeight() + 40) * 2) {
					buttonPressed = 2;
					mEventListener.onEventAccured();
					return super.onTouchEvent(event);
				}
				if (event.getY() > (440 + background.getHeight()) + (button1.getHeight() + 40) * 2 && event.getY() < (440 + background.getHeight()) + (button1.getHeight() + 40) * 3) {
					buttonPressed = 3;
					mEventListener.onEventAccured();
					return super.onTouchEvent(event);
				}
			}
		}
		return super.onTouchEvent(event);
	}

}

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

public class CreditView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

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
	private Bitmap soul7;
	private Bitmap soul1;
	private Bitmap soul2;
	private Bitmap soul3;
	private Bitmap soul4;
	private Bitmap soul5;
	private Bitmap soul6;
	private Bitmap button1;
	private Bitmap button2;
	private Bitmap button3;
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


	public CreditView(Context context, AttributeSet attrs){
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

		Mmusic = MediaPlayer.create(mContext, R.raw.credit);
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
		Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
		in = true;
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("-> FCT <-", "surfaceCreated");
		in = true;
		initparameters();
		Mmusic = MediaPlayer.create(mContext, R.raw.credit);
		Mmusic.setLooping(true);
		if(!bmute)
			Mmusic.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("-> FCT <-", "surfaceDestroyed");
		Mmusic.pause();
		in = false;

	}

	private void nDraw(Canvas canvas) {
		if(canvas == null)
			return;
		canvas.drawRGB(59, 28, 82);
		if(actualBackGround > 43){
			actualBackGround = 0;
		}
		actualBackGround++;
		InputStream assetInStream=null;
		try {
			assetInStream= mContext.getAssets().open("pframe"+actualBackGround+".png");
			Bitmap acc = BitmapFactory.decodeStream(assetInStream);
			background = Bitmap.createScaledBitmap(acc, acc.getWidth()*3, acc.getHeight()*3, false);
			canvas.drawBitmap(background, -150, getHeight()-background.getHeight(), null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(assetInStream!=null)
				System.out.println();
		}

		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.FILL);
		//canvas.drawRect(30, ((getHeight()-background.getHeight())+150), getWidth()/2-30, getHeight()-((80+(getHeight()-background.getHeight()))/2)+200-30, paint);
		//canvas.drawRect(getWidth()/2+30, ((getHeight()-background.getHeight())+150), getWidth()-30, getHeight()-((80+(getHeight()-background.getHeight()))/2)+200-30, paint);
		//canvas.drawRect(30, getHeight()-((80+(getHeight()-background.getHeight()))/2)+200+30, getWidth()/2-30, getHeight()-30, paint);
		//canvas.drawRect(getWidth()/2+30, getHeight()-((80+(getHeight()-background.getHeight()))/2)+200+30, getWidth()-30, getHeight()-30, paint);
		//paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(70);
		canvas.drawText("Alexis BEHIER 14501367", 60, 100, paint);

	}

	public void run(){
		Canvas c = null;
		while(in){
			try {
				cv_thread.sleep(40);
				try {
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
		//Mmusic.start();
		if ((cv_thread!=null) && (!cv_thread.isAlive())) {
			cv_thread = new Thread(this);
			cv_thread.start();
			Log.e("-FCT-", "cv_thread.start()");
			return;
		}
	}

	// fonction permettant de recuperer les evenements tactiles
	public boolean onTouchEvent (MotionEvent event) {
		Log.e("-OTE-", "They touch me");
		return super.onTouchEvent(event);
	}

}
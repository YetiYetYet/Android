package com.example.marjorie.color_match;

/**
 * Created by marjorie on 20/11/17.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class BestScoreView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private Resources 	mRes;
	private Context 	mContext;

	private     Thread  cv_thread;

	Paint paint;
	SurfaceHolder holder;

	volatile private boolean in = true;
	MediaPlayer Mmusic=new MediaPlayer();

	private boolean bmute = false;
	private String[] bestScore;


	/**
	 * The constructor called from the main JetBoy activity
	 *
	 * @param context
	 * @param attrs
	 */


	public BestScoreView(Context context, AttributeSet attrs){
		super(context, attrs);

		mContext	= context;
		mRes 		= mContext.getResources();

		// permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
		holder = getHolder();
		holder.addCallback(this);

		Mmusic = MediaPlayer.create(mContext, R.raw.applause);

		cv_thread   = new Thread(this);

		bestScore = ((BestScore)getContext()).getcurrentScore();

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
		bestScore = ((BestScore)getContext()).getcurrentScore();
		Mmusic = MediaPlayer.create(mContext, R.raw.applause);
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
		canvas.drawRGB(0,0,0);
		Paint myPaint = new Paint();
		myPaint.setColor(Color.rgb(255, 255, 255));
		myPaint.setStrokeWidth(3);
		myPaint.setTextSize(50);
		for(int i = 0; i <10; i++){
			Log.e("-> RUN <-", "bestScore" + bestScore.length);
			if(bestScore.length == i){
				return;
			}
			canvas.drawText(bestScore[i], 100, 100+((i*(getHeight()-200)/10)), myPaint);
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
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);
					}
				}
			} catch(Exception e) {
				//Log.e("-> RUN <-", "PB DANS RUN : " + e.getMessage());
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
		return super.onTouchEvent(event);
	}

}

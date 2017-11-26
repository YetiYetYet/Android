package com.example.marjorie.colormatch;

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
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

	public GameView(Context context, AttributeSet attrs){
		super(context, attrs);

		// permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
		holder = getHolder();
		holder.addCallback(this);

		cv_thread   = new Thread(this);

		initparameters();

	}




	// callback sur le cycle de vie de la surfaceview
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
		in = true;
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		Log.i("-> FCT <-", "surfaceCreated");
		in = true;
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("-> FCT <-", "surfaceDestroyed");
		in = false;

	}

	private void nDraw(Canvas canvas) {
		if(canvas == null)
			return;
		canvas.drawRGB(255,255,255);
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
		if ((cv_thread!=null) && (!cv_thread.isAlive())) {
			cv_thread = new Thread(this);
			cv_thread.start();
			Log.e("-FCT-", "cv_thread.start()");
			return;
		}
	}

}

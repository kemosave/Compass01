package jp.ac.ritsumei.ise.phy.exp2.is0429rs.compass01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder sHolder;
    private Thread thread;

    static final long FPS = 60;
    static final long FRAME_TIME = 1000 / FPS;

    public MySurfaceView(Context context) {
        super(context);
        initialize();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        sHolder = getHolder();
        sHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread = null;
    }

    @Override
    public void run() {

        long loopCount = 0;
        long waitTime = 0;

        Canvas c = sHolder.lockCanvas();
        c.drawColor(Color.WHITE);
        Paint p = new Paint();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.konpasu);

        Rect srcRect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Rect destRect = new Rect(0, 0, c.getWidth(), c.getHeight());

        //c.drawBitmap(bmp, srcRect, destRect, p);

        sHolder.unlockCanvasAndPost(c);

        long startTime = System.currentTimeMillis();

        while(thread != null) {
            try {

                sHolder.lockCanvas();

                c.rotate(-MainActivity.orientation[0], c.getWidth()/2, c.getHeight()/2);
                c.drawBitmap(bmp, srcRect, destRect, p);

                sHolder.unlockCanvasAndPost(c);

                waitTime = (loopCount * FRAME_TIME) - (System.currentTimeMillis() - startTime);

                if(waitTime > 0) {
                    Thread.sleep(waitTime);
                }
            } catch (InterruptedException e) {

            }
        }

    }

}


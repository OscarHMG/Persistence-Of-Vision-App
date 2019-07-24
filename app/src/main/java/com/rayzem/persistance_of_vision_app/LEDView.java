package com.rayzem.persistance_of_vision_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LEDView extends SurfaceView {
    private SurfaceHolder surfaceHolder;
    private LEDThread ledThread;
    boolean [][] data = null;

    private float diameterLeds, radiusLeds;

    private static Bitmap ledBitMap;

    private Paint paint;

    private String text = "HOLA";
    private int numPixels;
    private int cont = 0;


    public LEDView(Context context) {
        super(context);
        initLedView();
    }

    public LEDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLedView();
    }

    public LEDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLedView();
    }

    
    public void initLedView(){

        //Init paint
        paint = new Paint();

        surfaceHolder = getHolder();
        ledThread = new LEDThread(this);
        
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //Magin happens here!
                setDrawingTools();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                boolean retry = true;
                ledThread.setRunning(false);

                while (retry) {
                    try {
                        ledThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        Log.i("LedVIEW", "Error while the view is destroyed");
                    }
                }

            }
        });
        
    }



    private void setDrawingTools() {

        data = getWordPatternMatrix();

        diameterLeds = ((getHeight() / 2)) / numPixels;
        radiusLeds = diameterLeds / 2;

        ledBitMap = Bitmap.createBitmap((int) (diameterLeds + 0.5), (int)(diameterLeds + 0.5), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ledBitMap) ;

        paint.setColor(Color.RED);
        canvas.drawCircle(radiusLeds, radiusLeds, radiusLeds, paint);



        if(!ledThread.isRunning() && !ledThread.isAlive() ){
            ledThread.setRunning(true);
            ledThread.start();
        }
    }

    public void startTransitionsLeds(Canvas canvas){
        if(canvas != null){

            canvas.drawColor(Color.BLACK);
            for(int i = 0; i < numPixels; i++){
                if(data[cont][i])
                    canvas.drawBitmap(ledBitMap, (getWidth() - diameterLeds) / 2,diameterLeds * i, paint);

            }
            cont++;

            if(cont >= data.length)
                cont = 0;
        }
    }

    private boolean[][] getWordPatternMatrix(){
        boolean [][] result = null;

        Paint p = new Paint();


        int width = (int) (paint.measureText(text) + 0.5);
        float baseline = (Math.abs(paint.ascent()) + 0.5f);
        int height = (int)(baseline + paint.descent() + 0.5);

        numPixels = height;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, 0, baseline, p);
        Bitmap tmp = bitmap;

        result = new boolean[tmp.getWidth()][tmp.getHeight()];

        for(int i = 0; i<tmp.getWidth(); i++){
            for(int j = 0; j<tmp.getHeight(); j++){
                if(tmp.getPixel(i,j) != 0)
                    result[i][j] = true;

            }
        }

        return result;

    }




}

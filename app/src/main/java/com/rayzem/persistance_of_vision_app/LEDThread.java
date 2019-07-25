package com.rayzem.persistance_of_vision_app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Thread to handle the ON/OFF action of the leds.
 */
public class LEDThread extends Thread {


    //Control thread flag.
    private boolean isRunning;
    private LEDView ledView;
    private int test[];
    Paint paint = new Paint();
    private Bitmap ledBitMap;
    private int x;

    private float diameterLeds, radiusLeds;

    private static int cont = 0;


    public LEDThread(LEDView ledView){
        this.ledView = ledView;
    }


    public LEDThread(LEDView ledView, int test[], Bitmap ledBitmap, int width){
        this.ledView = ledView;
        this.test = test;
        this.ledBitMap = ledBitmap;
        this.x = width;

        diameterLeds = ledView.getDiameterLeds();

        paint.setColor(Color.RED);
    }

    /**
     * Here the magic begins!
     * Control if the led is ON or OFF
     */
    @Override
    public void run() {
        long ticksPS = 1000 / 30;
        long startTime;
        long sleepTime;

        while(isRunning){

            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            /*canvas = ledView.getHolder().lockCanvas();
            testTransition(canvas);*/
            //ledView.getHolder().unlockCanvasAndPost(canvas);
            //canvas = ledView.getHolder().lockCanvas();
            /*ledView.testTransition(canvas);



            ledView.getHolder().unlockCanvasAndPost(canvas);*/
            try {



                canvas = ledView.getHolder().lockCanvas();
                //ledView.testTransition(canvas);

                synchronized (ledView.getHolder()){
                    //ledView.startTransitionsLeds(canvas);

                    //ledView.testTransition(canvas);

                    testTransition(canvas);
                }
            }finally {
               if(canvas != null){
                   ledView.getHolder().unlockCanvasAndPost(canvas);
               }
            }

            delay(10);
            if(cont> 33){
                cont = 0;
                delay(120);
            }
            //After finishing drawing, delay to see a space.
            /*sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
                else {
                    //sleep(10);
                }
            } catch (Exception e) {
            }*/
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public LEDView getLedView() {
        return ledView;
    }

    public void setLedView(LEDView ledView) {
        this.ledView = ledView;
    }


    public void delay(int time){
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testTransition(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.BLACK);
            Log.i("OSCAR", "lol");


            if(cont > 33) {
                //cont = 0;

                canvas.drawColor(Color.BLACK);
                paint.setColor(Color.BLACK);
                for (int y = 0; y < 8; y++) {

                    canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2, diameterLeds * y, paint);
                }

                //delay(10000);

            }else{
                paint.setColor(Color.RED);
                for (int y = 0; y < 8; y++) {
                    if (test[y + cont] == 1) {

                        canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2, diameterLeds * y, paint);

                    }

                }
                //delay(10);
                cont = cont + 8;
            }


            /*canvas.drawColor(Color.BLACK);
            delay(1000);

            for (int y=0; y<8; y++){
                if(test[y + 8] == 1)
                    canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2,diameterLeds * y, paint);

            }
            canvas.drawColor(Color.BLACK);
            //ledThread.delay(1000);


            for (int y=0; y<8; y++){
                if(test[y + 16] == 1)
                    canvas.drawBitmap(ledBitMap, (x- diameterLeds) / 2,diameterLeds * y, paint);

            }
            canvas.drawColor(Color.BLACK);
            //ledThread.delay(1000);

            for (int y=0; y<8; y++){
                if(test[y + 24] == 1)
                    canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2,diameterLeds * y, paint);

            }
            canvas.drawColor(Color.BLACK);
            //ledThread.delay(1000);

            for (int y=0; y<8; y++){
                if(test[y + 32] == 1)
                    canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2,diameterLeds * y, paint);

            }

            //canvas.drawColor(Color.BLACK);



            //ledThread.delay(600);

            //delay(1);*/
        }
    }
}

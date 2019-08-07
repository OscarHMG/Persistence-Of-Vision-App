package com.rayzem.persistance_of_vision_app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Display;

/**
 * Thread to handle the ON/OFF action of the leds.
 */
public class LEDThread extends Thread {


    //Control thread flag.
    private boolean isRunning, flag = false;
    private LEDView ledView;
    private int test[];
    Paint paint = new Paint();
    private Bitmap ledBitMap;
    private int x;

    private float diameterLeds;

    private static int cont = 0, indexChar = 0, contNumWordTimes = 0, indexWord = 0;
    String pov_text = "OK";
    int result[] = null;

    private boolean firstTime = true;

    String[] splitted_pov_text;



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

        splitted_pov_text = pov_text.split("\\s+");
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
        long endTime;

        while(isRunning){

            Canvas canvas = null;
            startTime = System.currentTimeMillis();

            try {
                canvas = ledView.getHolder().lockCanvas();
                synchronized (ledView.getHolder()){
                    testTransition(canvas);
                }
            }finally {
               if(canvas != null){
                   if(cont> 33){
                       //When pattern finish, need to put all in black to emulate 'SPACE'
                       //canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

                       //Testing..
                       //delayMilliseconds(3);

                   }

                   ledView.getHolder().unlockCanvasAndPost(canvas);


                   //Testing..
                   /*if(cont>33){
                       cont = 0;
                       delayMilliseconds(12);
                       flag = true;
                   }else{
                       delayMilliseconds(1);
                       flag = false;
                   }*/
               }
            }

            endTime = System.currentTimeMillis();
            Log.i("CADA CICLO TOMA: ", ""+ (endTime - startTime));


            delayNanoSeconds(10000);
            //delayMilliseconds(1);
            if(cont> 33){
                cont = 0;
                delayMilliseconds(200);
                //delayMilliseconds(350);
                //50000, 240
                flag = true;
            }else {

                flag = false;
            }


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


    public void delayNanoSeconds(int time){
        try {
            sleep(0, time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void delayMilliseconds(int time){
        try {
            sleep(time);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void testTransition(Canvas canvas) {
        if (canvas != null) {
            //canvas.drawColor(Color.BLACK);
            canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

            if(flag){
                //Here, Means 2 things: I finish a char o a word.
                if (indexChar == pov_text.length() - 1 ) {
                    indexChar = 0;

                    //delayMilliseconds(10);

                    //TESTING..
                    /*if(contNumWordTimes == 30){ //TIME TO ADVANCE ANOTHER WORD
                        if(indexWord == splitted_pov_text.length - 1)
                            indexWord = 0;
                        else
                            indexWord = indexWord + 1;

                        contNumWordTimes = 0;
                        pov_text = splitted_pov_text[indexWord];
                        //Log.i("OSCAR", ". . .");
                    }else{
                        //Log.i("OSCAR", "..............");
                    }

                    contNumWordTimes = contNumWordTimes + 1;*/

                } else {
                    indexChar = indexChar + 1;

                    //Testing..
                    //delayMilliseconds(3);
                }

                result = LEDPattern.getPatternLetter(pov_text.charAt(indexChar));

                //TESTING..
                //canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);

            }else{

                //Only enter here, when the thread start at first time.
                if (firstTime) {
                    pov_text = splitted_pov_text[indexWord];
                    result = LEDPattern.getPatternLetter(pov_text.charAt(indexChar));
                    firstTime = false;
                }

                for (int y = 0; y < 8; y++) {
                    if (result[y + cont] == 1) {
                        canvas.drawBitmap(ledBitMap, (x - diameterLeds) / 2, diameterLeds * y, paint);

                        //Testing..
                        //delayMilliseconds(10);
                    }
                }
                cont = cont + 8;
            }
        }
    }
}

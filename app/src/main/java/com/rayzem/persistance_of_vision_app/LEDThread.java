package com.rayzem.persistance_of_vision_app;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Thread to handle the ON/OFF action of the leds.
 */
public class LEDThread extends Thread {

    //Control thread flag.
    private boolean isRunning;
    private LEDView ledView;


    public LEDThread(LEDView ledView){
        this.ledView = ledView;
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
            try {
                canvas = ledView.getHolder().lockCanvas();

                synchronized (ledView.getHolder()){
                    ledView.startTransitionsLeds(canvas);
                }
            }finally {
               if(canvas != null){
                   ledView.getHolder().unlockCanvasAndPost(canvas);
               }
            }

            //After finishing drawing, delay to see a space.
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
                else {
                    //sleep(10);
                }
            } catch (Exception e) {
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
}

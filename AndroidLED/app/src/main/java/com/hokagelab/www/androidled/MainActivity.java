package com.hokagelab.www.androidled;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private static final String TAG = "Android Things";
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 500;
    private static final int INTERVAL_BETWEEN_BLINKS_MS2 = 100;
    private static final String GPIO_PIN_NAME = "BCM12";
    private static final String GPIO_PIN_NAME2= "BCM21";
    private Handler mHandler = new Handler();
    private Gpio mLedGpio, mLedGpio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1. Create GPIO connection.
        PeripheralManagerService service = new PeripheralManagerService();
        try {
            mLedGpio = service.openGpio(GPIO_PIN_NAME);

            mLedGpio2 = service.openGpio(GPIO_PIN_NAME2);
            // Step 2. Configure as an output.
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio2.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.e(TAG, "Start Blink");
// Step 4. Repeat using a handler.
            mHandler.post(mBlinkRunnable);
            mHandler.post(mBlinkRunnable2);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }



    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLedGpio == null || mLedGpio2 == null) {
                return;
            }
            try {
                // Toggle the GPIO state
                mLedGpio.setValue(!mLedGpio.getValue());
                Log.d(TAG, "Lampu pertama itu " + mLedGpio.getValue());
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };

    private Runnable mBlinkRunnable2 = new Runnable() {
        @Override
        public void run() {
            if (mLedGpio == null || mLedGpio2 == null) {
                return;
            }
            try {
                // Toggle the GPIO state
                mLedGpio2.setValue(!mLedGpio2.getValue());
                Log.d(TAG, "Lampu Kedua itu " + mLedGpio2.getValue());
                mHandler.postDelayed(mBlinkRunnable2, INTERVAL_BETWEEN_BLINKS_MS2);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Step 4. Remove handler events on close.
        mHandler.removeCallbacks(mBlinkRunnable);
// Step 5. Close the resource.
        Log.e(TAG, "Closing LED");
        if (mLedGpio != null && mLedGpio2 != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }finally {
                mLedGpio = null;
            }
        }
    }


}

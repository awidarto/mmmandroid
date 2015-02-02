package com.kickstartlab.android.mmm.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by awidarto on 12/22/14.
 */
public class Utils {
    private static SharedPreferences sp;
    private static final String PREF_NAME = "stream_sp";
    public static final String IS_STREAM = "stream";

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static void setDataBooleanToSP(Context context, String to, boolean
            data) {
        sp = context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(to, data);
        editor.commit();
    }

    public static boolean getDataBooleanFromSP(Context context, String from) {
        sp = context.getSharedPreferences(PREF_NAME, 0);
        return sp.getBoolean(from, false);
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
}

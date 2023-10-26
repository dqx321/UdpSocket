package com.example.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author dai.qx
 * @time 2023-4-18
 * QQ:2445930742
 */
public class MyInterFace {

        public static String getAddress(Context context) {
            SharedPreferences sp = context.getSharedPreferences("Http", Context.MODE_PRIVATE);
            return sp.getString("Address", "");
        }


        public static void setAddress(Context context,String stations) {
            SharedPreferences sp =context.getSharedPreferences("Http", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Address", stations);
            editor.apply();
        }

}

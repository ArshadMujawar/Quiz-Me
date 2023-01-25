package com.asm.quizme;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    public static void setPref(String data, String key, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(key,Context.MODE_PRIVATE).edit();
        editor.putString(key,data); editor.apply();
    }

    public static String getPref(String key,Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(key,Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

    public static void setAmount(int data, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("amount",Context.MODE_PRIVATE).edit();
        editor.putInt("amount",data); editor.apply();
    }
    public static int getAmount(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("amount",Context.MODE_PRIVATE);
        return preferences.getInt("amount",0);
    }
}

/*Classe Java per inserire e rimuovere parametri dalle Shared
* Preferences dell'applicazione*/

package com.maxp.punkbeers;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceEditor {

    public final static String PREFS_NAME = "appname_prefs";

    public static void saveParams(Context context, String title, String sDesc, String lDesc, String imgUrl){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sliderTitle", title);
        editor.putString("sliderSDesc", sDesc);
        editor.putString("sliderLDesc", lDesc);
        editor.putString("imgUrl", imgUrl);
        editor.apply();
    }

    public static void setPanelAvailable(Context context, int show){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("showPanel", show++).apply();

    }

    public static Boolean getPanelAvailable(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Boolean showPanel = sharedPreferences.getBoolean("showPanel", false);
        return showPanel;
    }

    public static String getTitle(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String title = sharedPreferences.getString("sliderTitle", "");
        return title;
    }

    public static String getSDesc(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String sDesc = sharedPreferences.getString("sliderSDesc", "");
        return sDesc;
    }

    public static String getLDesc(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lDesc = sharedPreferences.getString("sliderLDesc", "");
        return lDesc;
    }

    public static String getImgUrl(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("imgUrl", "");
        return url;
    }

    public static void registerPref(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPref(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}

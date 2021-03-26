package com.example.saktikrupafurniture.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_ID= "ID";
    static final String PREF_USERNAME= "USERNAME";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setData(Context ctx, int id,String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_ID, id);
        editor.putString(PREF_USERNAME, userName);
        editor.commit();
    }

    public static int getId(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_ID,0);
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
    }



    public static void removeUserName(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear().commit();
    }
}

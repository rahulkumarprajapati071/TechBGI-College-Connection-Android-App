package com.example.techbgi.sharedsession;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceSession {
    private static String TAG = SharedPreferenceSession.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int MODE_PRIVATE = 0;

    private static final String PREF_NAME = "TechBGILogin";
    private static final String KEY_IS_LOGGED_IN = "who";

    public SharedPreferenceSession(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setWho(String who){
        editor.putString(KEY_IS_LOGGED_IN,who);
        editor.commit();
    }

    public String getWho(){
        return pref.getString(KEY_IS_LOGGED_IN,"none");
    }
}

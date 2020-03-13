package com.agungadi.myapplication.Helper;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final String INTRO = "intro";
    private final String NAME = "name";
    private final String email = "email";
    private final String id = "id";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
    public boolean getIsLogin() {
        return app_prefs.getBoolean(INTRO, false);
    }

    public void putName(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAME, loginorout);
        edit.commit();
    }
    public String getName() {
        return app_prefs.getString(NAME, "");
    }

    public void putEmail(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(email, loginorout);
        edit.commit();
    }
    public String getEmail() {
        return app_prefs.getString(email, "");
    }
    public void putId(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(id, loginorout);
        edit.commit();
    }

    public String getId(){
        return  app_prefs.getString(id, "");
    }


}


package com.josholadele.huaweitest.cache;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.josholadele.huaweitest.HuaweiTestApplication;

import java.util.Map;
import java.util.Set;

public class SharedPref {

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public SharedPref() {
        Context context = HuaweiTestApplication.Companion.getInstance().getApplicationContext();
        pref = context.getSharedPreferences("SharedPref", MODE_PRIVATE);
        editor = pref.edit();
    }


    public void putInt(String name, int value) {
        editor.putInt(name, value).commit();
    }

    public int getInt(String name, int defaultValue) {
        return pref.getInt(name, defaultValue);
    }

    public void putString(String name, String value) {
        editor.putString(name, value).commit();
    }
    public void removeString(String name) {
        editor.remove(name).commit();
    }

    public String getString(String name, String defaultValue) {
        return pref.getString(name, defaultValue);
    }
}

package com.volvo.smartkeywatch.Utils;

import android.util.Log;
import android.view.ViewGroup;
import com.google.android.material.snackbar.Snackbar;

public class GlobalClass {

    public static void snackbar(ViewGroup parentLayout, String message){
//        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
        Log.e("GlobalClass", message);
    }

}

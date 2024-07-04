package com.tomandjerry.tomandjerryv2.Activities;

import android.app.Application;

import com.tomandjerry.tomandjerryv2.R;
import com.tomandjerry.tomandjerryv2.Utilities.MyBackgroundMusic;
import com.tomandjerry.tomandjerryv2.Utilities.MySharedPreferences;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPreferences.init(this);
        MyBackgroundMusic.init(this);
        MyBackgroundMusic.getInstance().setResourceId(R.raw.sn_background);
    }
}

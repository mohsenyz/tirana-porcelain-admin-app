package com.tiranaporcelain.admin;

import android.app.Application;
import android.content.Context;

import com.tiranaporcelain.admin.utils.DaoManager;
import com.tiranaporcelain.admin.utils.TypefaceUtils;

import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mphj on 10/14/2017.
 */

public class AccountryApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran.ttf")
                .build()
        );
        DaoManager.init(this);
        Toasty.Config.getInstance()
                .setToastTypeface(TypefaceUtils.def())
                .setTextSize(14)
                .apply();
    }


    public static Context context(){
        if (context == null)
            throw new NullPointerException("Context is null");
        return context;
    }
}

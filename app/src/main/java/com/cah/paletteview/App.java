package com.cah.paletteview;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.logging.Logger;

/**
 * Created by chengaihua on 2017/11/21.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .tag("CAH")
                .build();
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(strategy));
    }
}

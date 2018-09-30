package com.gianlu.fidal;

import com.gianlu.commonutils.Analytics.AnalyticsApplication;

public class ThisApplication extends AnalyticsApplication {
    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}

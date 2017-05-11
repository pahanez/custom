package com.pahanez.custom.common.util;

import java.util.Objects;

/**
 * Created by pindziukou on 11/05/2017.
 */

public class BuildConfig {

    private static final boolean DEBUG = true;

    private static Config config;

    /** should be configured based on presentation module gradle configureation */
    public static void setConfig(Config config) {
        Objects.requireNonNull(config, "config cannot be null");
        BuildConfig.config = config;
    }

    public static boolean DEBUG() {
        if(config != null) {
            return config.debugable();
        }
        return DEBUG;
    }


    public interface Config {
        boolean debugable();
    }
}

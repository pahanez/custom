package com.pahanez.custom.common.util;

import java.util.Objects;

public class L {

    private static ILog logger = new DefaultLogger();

    public static void setLogger(ILog logger) {
        Objects.requireNonNull(logger);
        L.logger = logger;
    }

    public static void v(String tag, String msg) {
        logger.v(tag, msg);
    }


    /** Output only in case when debug build */
    public static void d(String tag, String msg) {
        if(BuildConfig.DEBUG()) {
            logger.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        logger.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        logger.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        logger.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable error) {
        logger.e(tag, msg, error);
    }



}

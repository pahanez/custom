package com.pahanez.custom.common.util;


public class DefaultLogger implements ILog {

    @Override
    public void v(String tag, String msg) {
        System.out.println(String.format("%s | %s", tag, msg));
    }

    @Override
    public void d(String tag, String msg) {
        System.out.println(String.format("%s | %s", tag, msg));
    }

    @Override
    public void i(String tag, String msg) {
        System.out.println(String.format("%s | %s", tag, msg));
    }

    @Override
    public void w(String tag, String msg) {
        System.err.println(String.format("%s | %s", tag, msg));
    }

    @Override
    public void e(String tag, String msg) {
        System.err.println(String.format("%s | %s", tag, msg));
    }

    @Override
    public void e(String tag, String msg, Throwable error) {
        System.err.println(String.format("%s | %s", tag, msg));
        error.printStackTrace();
    }
}

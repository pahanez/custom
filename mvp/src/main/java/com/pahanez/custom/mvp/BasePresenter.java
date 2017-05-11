package com.pahanez.custom.mvp;

import android.support.annotation.UiThread;

public interface BasePresenter <V extends BaseView, ViewState> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView(boolean retainInstance);
}

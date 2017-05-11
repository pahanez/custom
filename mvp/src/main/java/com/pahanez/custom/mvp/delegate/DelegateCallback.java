package com.pahanez.custom.mvp.delegate;

import android.support.annotation.NonNull;

import com.pahanez.custom.mvp.BasePresenter;
import com.pahanez.custom.mvp.BaseView;

/**
 delegate -> ui (activity, fragment, view)
 */

public interface DelegateCallback <V extends BaseView, P extends BasePresenter<V, ?>> {
    @NonNull P providePresenter();
    @NonNull V provideView();
}

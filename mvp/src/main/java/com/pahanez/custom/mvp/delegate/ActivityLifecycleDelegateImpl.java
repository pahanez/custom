package com.pahanez.custom.mvp.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pahanez.custom.common.util.BuildConfig;
import com.pahanez.custom.common.util.L;
import com.pahanez.custom.mvp.BasePresenter;
import com.pahanez.custom.mvp.BaseView;

import java.util.Objects;

class ActivityLifecycleDelegateImpl <V extends BaseView, P extends BasePresenter<V, ?>> implements ActivityLifecycleDelegate<V, P> {

    private static final boolean DEBUG = BuildConfig.DEBUG();
    private static final String TAG = ActivityLifecycleDelegateImpl.class.getSimpleName();
    private static final String KEY_VIEW_ID = "com.pahanez.custom.mvp.id";


    private String mStoredViewId;
    private DelegateCallback<V, P> mDelegateCallback;
    private Activity mActivity;
    private boolean mKeepPresenterInstance;
    private P mPresenter;

    ActivityLifecycleDelegateImpl(@NonNull Activity activity,
                                   @NonNull DelegateCallback<V, P> delegateCallback) {
        this(activity, delegateCallback, true);
    }

    ActivityLifecycleDelegateImpl(@NonNull Activity activity,
                                   @NonNull DelegateCallback<V, P> delegateCallback, boolean keepPresenterInstance) {

        Objects.requireNonNull(activity, "activity cannot be null");
        Objects.requireNonNull(delegateCallback, "delegate callback cannot be null");

        mDelegateCallback = delegateCallback;
        mActivity = activity;
        mKeepPresenterInstance = keepPresenterInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        if (mKeepPresenterInstance && bundle != null) {
            mStoredViewId = bundle.getString(KEY_VIEW_ID);
        }

        if (DEBUG) {
            L.d(TAG,
                    "Restored view ID = " + mStoredViewId + " for View: " + mDelegateCallback.provideView());
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mPresenter = null;
        mActivity = null;
        mDelegateCallback = null;
    }

    @Override
    public void onContentChanged() {}

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }
}

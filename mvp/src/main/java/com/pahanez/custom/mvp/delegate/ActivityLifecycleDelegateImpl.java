package com.pahanez.custom.mvp.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
        boolean viewStateWillBeRestored = false;
        if (mStoredViewId == null) {
            // No presenter available,
            // Activity is starting for the first time (or keepPresenterInstance == false)
            mPresenter = createViewIdAndCreatePresenter();
            if (DEBUG) {
                L.d(TAG, "new Presenter instance created: "
                        + mPresenter
                        + " for "
                        + mDelegateCallback.provideView());
            }
        } else {
            presenter = PresenterManager.getPresenter(activity, mosbyViewId);
            if (mPresenter == null) {
                // Process death,
                // hence no presenter with the given viewState id stored, although we have a viewState id
                mPresenter = createViewIdAndCreatePresenter();
                if (DEBUG) {
                    L.d(TAG,
                            "No Presenter instance found in cache, although Strored View ID present. This was caused by process death, therefore new Presenter instance created: "
                                    + mPresenter);
                }
            } else {
                viewStateWillBeRestored = true;
                if (DEBUG) {
                    L.d(TAG, "Presenter instance reused from internal cache: " + mPresenter);
                }
            }
        }

        // presenter is ready, so attach viewState
        V view = mDelegateCallback.provideView();
        Objects.requireNonNull(view, "view cannot be null");

        /*if (viewStateWillBeRestored) {
            mDelegateCallback.setRestoringViewState(true); todo check
        }*/

        mPresenter.attachView(view);

        /*if (viewStateWillBeRestored) {
            delegateCallback.setRestoringViewState(false);
        }*/

        if (DEBUG) {
            L.d(TAG,
                    "View attached to Presenter. View: " + view + "   Presenter: " + mPresenter);
        }
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

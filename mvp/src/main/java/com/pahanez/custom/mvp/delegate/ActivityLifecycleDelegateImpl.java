package com.pahanez.custom.mvp.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pahanez.custom.common.util.BuildConfig;
import com.pahanez.custom.common.util.L;
import com.pahanez.custom.mvp.BasePresenter;
import com.pahanez.custom.mvp.BaseView;
import com.pahanez.custom.mvp.delegate.presentermanager.PresenterManager;

import java.util.Objects;
import java.util.UUID;

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
            mPresenter = PresenterManager.getPresenter(mActivity, mStoredViewId);
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
            delegateCallback.setRestoringViewState(false); todo check
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
    public void onResume() {}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mKeepPresenterInstance) {
            outState.putString(KEY_VIEW_ID, mStoredViewId);
            if (DEBUG) {
                L.d(TAG, "Saving View ID into Bundle. ViewId: " + mStoredViewId);
            }
        }
    }

    @Override
    public void onPause() {}

    @Override
    public void onStop() {
        boolean retainPresenterInstance = retainPresenterInstance(mKeepPresenterInstance, mActivity);
        mPresenter.detachView(retainPresenterInstance);

        if (DEBUG) {
            L.d(TAG, "detached VIEW from Presenter. VIEW: "
                    + mDelegateCallback.provideView()
                    + "   Presenter: "
                    + mPresenter);
        }

        if (!retainPresenterInstance) {
            if (mStoredViewId != null) { // mosbyViewId == null if keepPresenterInstance == false
                PresenterManager.remove(mActivity, mStoredViewId);
            }
            L.d(TAG, "Destroying Presenter permanently " + mPresenter);
        }
    }

    private boolean retainPresenterInstance(boolean keepPresenterInstance, Activity activity) {
        return keepPresenterInstance && (activity.isChangingConfigurations()
                || !activity.isFinishing());
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

    private P createViewIdAndCreatePresenter() {

        P presenter = mDelegateCallback.providePresenter();

        Objects.requireNonNull(presenter, "activity should provide presenter");

        if (mKeepPresenterInstance) {
            mStoredViewId = UUID.randomUUID().toString();
            PresenterManager.putPresenter(mActivity, mStoredViewId, presenter);
        }
        return presenter;
    }
}

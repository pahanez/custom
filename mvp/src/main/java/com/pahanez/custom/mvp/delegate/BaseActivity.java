package com.pahanez.custom.mvp.delegate;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pahanez.custom.mvp.BasePresenter;
import com.pahanez.custom.mvp.BaseView;

public abstract class BaseActivity <V extends BaseView, P extends BasePresenter<V, ?>> extends AppCompatActivity implements BaseView, DelegateCallback<V, P> {

    private ActivityLifecycleDelegate <V, P> mLifeCycleDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycleDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLifecycleDelegate().onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getLifecycleDelegate().onRestart();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        getLifecycleDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLifecycleDelegate().onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getLifecycleDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getLifecycleDelegate().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getLifecycleDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycleDelegate().onDestroy();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getLifecycleDelegate().onContentChanged();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return getLifecycleDelegate().onRetainCustomNonConfigurationInstance();
    }

    @NonNull
    protected final ActivityLifecycleDelegate<V, P> getLifecycleDelegate() {
        if (mLifeCycleDelegate == null) {
            mLifeCycleDelegate = new ActivityLifecycleDelegateImpl<>(this, this);
        }
        return mLifeCycleDelegate;
    }

    @NonNull @Override public V provideView() {
        try {
            return (V) this;
        } catch (ClassCastException e) {
            throw new RuntimeException("wrong view interface", e);
        }
    }
}

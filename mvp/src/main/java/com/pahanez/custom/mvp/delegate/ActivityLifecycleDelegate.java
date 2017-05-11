package com.pahanez.custom.mvp.delegate;

import android.os.Bundle;

import com.pahanez.custom.mvp.BasePresenter;
import com.pahanez.custom.mvp.BaseView;

interface ActivityLifecycleDelegate <V extends BaseView, P extends BasePresenter<V, ?>> {

    void onCreate(Bundle bundle);
    void onStart();   void onRestart();
    void onPostCreate(Bundle savedInstanceState);
    void onResume();

    void onSaveInstanceState(Bundle outState);
    void onPause();
    void onStop();
    void onDestroy();



    /** This hook is called whenever the content view of the screen changes
        (due to a call to Window.setContentView or Window.addContentView) */
    void onContentChanged();


    /** Store custom state object durting config changes */
    Object onRetainCustomNonConfigurationInstance();

}

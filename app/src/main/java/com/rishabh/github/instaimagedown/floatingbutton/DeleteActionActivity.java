package com.rishabh.github.instaimagedown.floatingbutton;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.rishabh.github.instaimagedown.R;
import com.rishabh.github.instaimagedown.service.CustomFloatingViewService;

public class DeleteActionActivity extends Activity implements ServiceConnection, DeleteActionFragment.DeleteActionCallback {

    private static final String TAG = "DeleteActionActivity";

    private static final String FRAGMENT_TAG_DELETE_ACTION = "delete_action";

    private Service mTargetService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_action);

        if (savedInstanceState == null) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.container, DeleteActionFragment.newInstance(), FRAGMENT_TAG_DELETE_ACTION);
            ft.commit();
        }

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mTargetService = ((CustomFloatingViewService.CustomFloatingViewServiceBinder) service).getService();

        if (mTargetService != null) {
            unbindService(this);
            mTargetService.stopSelf();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mTargetService = null;
    }

    @Override
    public void clearFloatingView() {
        bindService(new Intent(this, CustomFloatingViewService.class), this, Context.BIND_AUTO_CREATE);
    }
}
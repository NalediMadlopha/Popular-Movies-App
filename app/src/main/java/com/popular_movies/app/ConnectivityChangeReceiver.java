package com.popular_movies.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by root on 2016/09/13.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, context.getClass().getName(), Toast.LENGTH_LONG).show();
    }
}

package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.DetailsActivity;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }
  private String LOG_TAG = StockIntentService.class.getSimpleName();

  private android.os.Handler mHandler;
  @Override
  public void onCreate() {
    super.onCreate();
    mHandler = new Handler();

  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    StockTaskService stockTaskService = new StockTaskService(this);
    Bundle args = new Bundle();
    if (intent.getStringExtra("tag").equals("display")){
     // Log.i(LOG_TAG, "Inside display method");
      Intent myIntent =new Intent(this, DetailsActivity.class);
      myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      myIntent.putExtra("symbol",intent.getStringExtra("symbol"));
      startActivity(myIntent);
      return ;
    }

    if (intent.getStringExtra("tag").equals("add")){
      args.putString("symbol", intent.getStringExtra("symbol"));
    }
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    final int result = stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));

    mHandler.post(new Runnable() {
      @Override
      public void run() {
        if (result == 3) {
          Toast.makeText(StockIntentService.this, getResources().getString(R.string.stock_does_not_exist_warning), Toast.LENGTH_LONG).show();
        }
      }
    });
  }
}

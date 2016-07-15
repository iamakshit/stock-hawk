package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.objects.StockHistoricalData;
import com.sam_chordas.android.stockhawk.rest.DateUtils;
import com.sam_chordas.android.stockhawk.service.task.StockHistoricalDataTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity {

    public static String TAG = DetailsActivity.class.getSimpleName();
    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        plot = (XYPlot) findViewById(R.id.plot);
        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");
        ArrayList<StockHistoricalData> stockHistoricalDatas= fetchStockHistoricalData(symbol);
        Log.i(TAG,"stockHistoricalDatas :: size "+stockHistoricalDatas.size());

        // create a couple arrays of y-values to plot:
        ArrayList<Number> series1Numbers = new ArrayList<>();
      //  int i=0;
       Collections.reverse(stockHistoricalDatas);
        for(StockHistoricalData stockHistoricalData:stockHistoricalDatas)
        {
            Log.i(TAG,"Date {}"+stockHistoricalData.getDate());
            series1Numbers.add(stockHistoricalData.getValue());
       //     ++i;
        }

        Log.i(TAG,"series1Numbers size = "+series1Numbers.size());
     //   Log.i(TAG,"series1Numbers string = "+series1Numbers.toString());

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(series1Numbers,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(), R.xml.line_point_formatter_with_labels);

        // add an "dash" effect to the series2 line:

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        //plot.addSeries(series2, series2Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_details, menu);

        // Adapter
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.order, android.R.layout.simple_spinner_dropdown_item);

        // Callback
        ActionBar.OnNavigationListener callback = new ActionBar.OnNavigationListener() {
            String[] items = getResources().getStringArray(R.array.order); // List items from res
            @Override
            public boolean onNavigationItemSelected(int position, long id) {
                // Do stuff when navigation item is selected
                  Log.i(TAG, "Item selected "+items[position]);
                Log.i(TAG,"Computed Date =>"+ DateUtils.computeDateByString(items[position]));
                return true;
            }
        };

        // Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setListNavigationCallbacks(adapter, callback);
        actionBar.setTitle(getTitle());

        return true;
    }

    public  ArrayList<StockHistoricalData>  fetchStockHistoricalData(String symbol) {

        ArrayList<String> list = new ArrayList<String>();
      //  Log.i(TAG,"Calling task");
        StockHistoricalDataTask task;
        task = new StockHistoricalDataTask();
        int corePoolSize = 60;
        int maximumPoolSize = 80;
        int keepAliveTime = 10;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
       // Log.i("DetailsActivity", "Refresh Action being called");

        HashMap<String,String> map = new HashMap<>();
        String endDate = DateUtils.getCurrentDate();
        Log.i(TAG, "endDate = " + endDate + " symbol = " + symbol);

        map.put("symbol",symbol);
        map.put("startDate", "2016-07-01");
        map.put("endDate", endDate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, map);
        else
            task.execute(map);

        try {
            return task.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void dumpIntent(Intent i){

        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(TAG,"Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(TAG,"[" + key + "=" + bundle.get(key)+"]");
            }
            Log.e(TAG,"Dumping Intent end");
        }
    }
}
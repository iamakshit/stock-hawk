package com.sam_chordas.android.stockhawk.service;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.squareup.okhttp.OkHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by akshitgupta on 03/07/16.
 */
public class StockHistoryTaskService extends GcmTaskService {

    private OkHttpClient client = new OkHttpClient();
    private Context mContext;
    private StringBuilder mStoredSymbols = new StringBuilder();
    private boolean isUpdate;

    @Override
    public int onRunTask(TaskParams taskParams) {
        Cursor initQueryCursor;
        if (mContext == null){
            mContext = this;
        }
        StringBuilder urlStringBuilder = new StringBuilder();
        String stockInput = taskParams.getExtras().getString("symbol");
        String startDate = taskParams.getExtras().getString("startDate");
        String endDate = taskParams.getExtras().getString("endDate");

        try{
            // Base URL for the Yahoo query
            urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.quotes where symbol "
                    + "in (", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

package com.sam_chordas.android.stockhawk.service.task;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by akshitgupta on 03/07/16.
 */
public class StockHistoricalDataTask extends AsyncTask<TaskParams, Void,Void> {

    private OkHttpClient client = new OkHttpClient();
    String fetchData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    @Override
    protected Void doInBackground(TaskParams... params) {
        TaskParams taskParams = params[0];
        StringBuilder urlStringBuilder = new StringBuilder();
        String stockInput = taskParams.getExtras().getString("symbol");
        String startDate = taskParams.getExtras().getString("startDate");
        String endDate = taskParams.getExtras().getString("endDate");
        urlStringBuilder.append("https://query.yahooapis.com/v1/public/yql?q=");
        try {
            urlStringBuilder.append(URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = ", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlStringBuilder.append("\""+stockInput+"\"");
        urlStringBuilder.append("and startDate = ");
        urlStringBuilder.append("\""+startDate+"\"");
        urlStringBuilder.append("and endDate = ");
        urlStringBuilder.append("\""+endDate+"\"");

        String urlString;
        String getResponse;
        int result = GcmNetworkManager.RESULT_FAILURE;
        if (urlStringBuilder != null) {
            urlString = urlStringBuilder.toString();
            try {
                getResponse = fetchData(urlString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
            return null;
    }
}

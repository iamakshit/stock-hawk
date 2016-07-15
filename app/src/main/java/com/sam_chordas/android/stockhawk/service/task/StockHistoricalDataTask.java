package com.sam_chordas.android.stockhawk.service.task;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.sam_chordas.android.stockhawk.objects.StockHistoricalData;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akshitgupta on 03/07/16.
 */
public class StockHistoricalDataTask extends AsyncTask<HashMap<String,String>, Void, ArrayList<StockHistoricalData> > {
    private String LOG_TAG = StockHistoricalDataTask.class.getSimpleName();

    private OkHttpClient client = new OkHttpClient();
    String fetchData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    @Override
    protected  ArrayList<StockHistoricalData>  doInBackground(HashMap<String, String>... params) {
        ArrayList<StockHistoricalData> batchOperations =  new ArrayList<>();
        HashMap<String,String> map = params[0];
        StringBuilder urlStringBuilder = new StringBuilder();
        String stockInput = map.get("symbol");
        String startDate = map.get("startDate");
        String endDate = map.get("endDate");
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
        urlStringBuilder.append("&format=json&diagnostics=true&env=http://datatables.org/alltables.env");
        String urlString;
        String getResponse;
        int result = GcmNetworkManager.RESULT_FAILURE;
        if (urlStringBuilder != null) {
            urlString = urlStringBuilder.toString();
            try {
                getResponse = fetchData(urlString);
                batchOperations=   Utils.quoteJsonToContentVals(getResponse, "historicalData");
                Log.i(LOG_TAG, "batchOperations size "+batchOperations);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
            return batchOperations;
    }

    @Override
    protected void onPostExecute (ArrayList<StockHistoricalData>  stockHistoricalDatas){
        Log.i(LOG_TAG, "Inside onPostExecute method");
        Log.i(LOG_TAG, "stockHistoricalDatas size "+stockHistoricalDatas.size());

        super.onPostExecute(stockHistoricalDatas);
    }
}

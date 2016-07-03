package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.sam_chordas.android.stockhawk.R;

public class DetailsActivity extends AppCompatActivity {

    public static String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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
                  Log.i(TAG, "Item selected "+items[position]); // Debug
                return true;
            }
        };

        // Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setListNavigationCallbacks(adapter, callback);
        actionBar.setTitle(getTitle());

        return true;
    }
}
package com.sam_chordas.android.stockhawk.objects;

import java.util.Date;

/**
 * Created by akshitgupta on 15/07/16.
 */
public class StockHistoricalData {


    String symbol;
    String dateToString;
    Date date;
    Float value;

    public StockHistoricalData(String symbol, String dateToString, Date date, Float value) {
        this.symbol = symbol;
        this.dateToString = dateToString;
        this.date = date;
        this.value = value;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}

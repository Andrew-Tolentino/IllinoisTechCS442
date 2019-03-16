package com.example.stockwatch;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {

    // Initialize List of Stocks
    private List<Stock> stockList = new ArrayList<>();

    // Declare MainActivity variable for reference
    private MainActivity ma;

    // Constructor which will connect list to datasource and ma to the Main Activity
    public StockAdapter(List<Stock> stockList, MainActivity ma) {
        this.stockList = stockList;
        this.ma = ma;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflate itemView with the layout of the stock view holder
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_view_holder, viewGroup, false);

        // Link onClick and onLongClick Listeners
        itemView.setOnClickListener(this.ma);
        itemView.setOnLongClickListener(this.ma);

        // Return the ViewHolder passing its layout as the argument
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, int i) {
        // Initialize the stock ViewHolder template with the corresponding elements of the Stock isntance from stockList
        Stock currStock = this.stockList.get(i);

        // Format to get Doubles to 2 decimal places
        DecimalFormat twoDecimals = new DecimalFormat("0.00");

        // If the current stock price change is positive
        if (currStock.getPriceChange() >= 0) {
            // Set TextView objects in the ViewHolder with the color green

            // Set stock's symbol
            stockViewHolder.stockSymbol.setText(currStock.getStockSymbol());
            stockViewHolder.stockSymbol.setTextColor(Color.parseColor("#32CD32"));

            // Set stock's company name
            stockViewHolder.companyName.setText(currStock.getCompanyName());
            stockViewHolder.companyName.setTextColor(Color.parseColor("#32CD32"));

            // Set the stock's price
            stockViewHolder.price.setText(twoDecimals.format(currStock.getPrice()));
            stockViewHolder.price.setTextColor(Color.parseColor("#32CD32"));

            // Set the remaining information of the stock
            String extraInfo = "▲" + " " + twoDecimals.format(currStock.getPriceChange()) + " (" + twoDecimals.format(currStock.getChangePercentage()) + "%)";
            stockViewHolder.extraInfo.setText(extraInfo);
            stockViewHolder.extraInfo.setTextColor(Color.parseColor("#32CD32"));

        }

        // If the current stock price change is negative
        else {
            // Set TextView objects in the ViewHolder with the color red

            // Set stock's symbol
            stockViewHolder.stockSymbol.setText(currStock.getStockSymbol());
            stockViewHolder.stockSymbol.setTextColor(Color.parseColor("#FF4C4C"));

            // Set stock's company name
            stockViewHolder.companyName.setText(currStock.getCompanyName());
            stockViewHolder.companyName.setTextColor(Color.parseColor("#FF4C4C"));

            // Set the stock's price
            stockViewHolder.price.setText(twoDecimals.format(currStock.getPrice()));
            stockViewHolder.price.setTextColor(Color.parseColor("#FF4C4C"));

            // Set the remaining information of the stock
            String extraInfo = "▼" + " " + twoDecimals.format(currStock.getPriceChange()) + " (" + twoDecimals.format(currStock.getChangePercentage()) + "%)";
            stockViewHolder.extraInfo.setText(extraInfo);
            stockViewHolder.extraInfo.setTextColor(Color.parseColor("#FF4C4C"));
        }



    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}

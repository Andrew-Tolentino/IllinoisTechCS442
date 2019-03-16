package com.example.stockwatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

public class StockViewHolder extends ViewHolder {
    // Declare TextView objects that will represent the TextViews in the stock_view_holder layout
    public TextView stockSymbol;
    public TextView companyName;
    public TextView price;
    public TextView extraInfo;


    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        // Link the declared TextView Objects to the TextView components in the stock_view_holder layout
        stockSymbol = itemView.findViewById(R.id.stockSymbol);
        companyName = itemView.findViewById(R.id.companyName);
        price = itemView.findViewById(R.id.price);
        extraInfo = itemView.findViewById(R.id.extra_info);
    }
}

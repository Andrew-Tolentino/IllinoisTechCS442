package com.example.stockwatch;

public class Stock {
    private String stockSymbol;
    private String companyName;
    private double price;
    private double priceChange;
    private double changePercentage;

    public Stock(String stockSymbol, String companyName, double price, double priceChange, double changePercentage) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.changePercentage = changePercentage;
    }

    // Getter for the stock's symbol
    public String getStockSymbol() {
        return this.stockSymbol;
    }

    // Setter for the stock's symbol
    public void setStockSymbol(String symb) {
        this.stockSymbol = symb;
    }

    // Getter for the stock's company name
    public String getCompanyName() {
        return this.companyName;
    }

    // Setter for the stock's company name
    public void setCompanyName(String name) {
        this.companyName = name;
    }

    // Getter for the stock's price
    public double getPrice() {
        return this.price;
    }

    // Setter for the stock's price
    public void setPrice(double p) {
        this.price = p;
    }

    // Getter for the stock's change in price
    public double getPriceChange() {
        return this.priceChange;
    }

    // Setter for the stock's change in price
    public void setPriceChange(double change) {
        this.priceChange = change;
    }

    // Getter for the stock's change in price percentage
    public double getChangePercentage() {
        return this.changePercentage;
    }

    // Setter for the stock's change in prince percentage
    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }


}

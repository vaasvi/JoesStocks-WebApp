package vaasvich_CSCI201_Assignment4;

import java.util.List;

public class PortfolioResponse {
    private List<StockData> stocks;
    private double cashBalance;
    private double accountValue;

    // Constructor
    public PortfolioResponse(List<StockData> stocks, double cashBalance, double accountValue) {
        this.stocks = stocks;
        this.cashBalance = cashBalance;
        this.accountValue = accountValue;
    }

    // Getters
    public List<StockData> getStocks() {
        return stocks;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public double getAccountValue() {
        return accountValue;
    }
}

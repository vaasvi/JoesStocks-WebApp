package vaasvich_CSCI201_Assignment4;

public class StockData {
    private String symbol;
    private String companyName;
    private int quantity;
    private double change;
    private double avgCostPerShare;
    private double totalCost;
    private double currentPrice;
    private double marketValue;

    // Constructors, getters, and setters
    public StockData(String symbol, String companyName, int quantity, double avgCostPerShare, double totalCost,double change, double currentPrice, double marketValue) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.quantity = quantity;
        this.avgCostPerShare = avgCostPerShare;
        this.totalCost = totalCost;
        this.change = change;
        this.currentPrice = currentPrice;
        this.marketValue = marketValue;
    }

    // Getters and setters for each field
    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAvgCostPerShare() {
        return avgCostPerShare;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getCurrentPrice() {
        return currentPrice;
    }

    public double getMarketValue() {
        return marketValue;
    }
}

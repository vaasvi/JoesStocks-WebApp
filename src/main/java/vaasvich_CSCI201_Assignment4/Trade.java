package vaasvich_CSCI201_Assignment4;

public class Trade {
	private int tradeId;
	private String username;
    

	private String ticker;
    private int numStock;
    private double price;
    private String tradeType;
	public Trade(int tradeId, String username, String ticker, int numStock, double price) {
		this.tradeId = tradeId;
		this.username = username;
        this.ticker = ticker;
        this.numStock = numStock;
        this.price = price;
        this.tradeType = tradeType;
    }
    
    

	public int getTradeId() {
		return tradeId;
	}

	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTicker() {
		System.out.println("inside get ticker: "+ticker);
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public int getNumStock() {
		return numStock;
	}

	public void setNumStock(int numStock) {
		this.numStock = numStock;
	}
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}



	public String getTradeType() {
		return tradeType;
	}



	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

}

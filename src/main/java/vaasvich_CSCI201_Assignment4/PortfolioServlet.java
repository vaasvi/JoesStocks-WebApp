package vaasvich_CSCI201_Assignment4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;


@WebServlet("/PortfolioServlet")
public class PortfolioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        System.out.println("inside portfolioservlet");
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("{\"error\":\"User not logged in.\"}");
            out.close();
            return;
        }

        String username = (String) session.getAttribute("username");
        System.out.println("PORTFOLIO SERVLET: "+ username);
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/JoeStocks", "root", "Tusharchoudhary12!");
        	     PreparedStatement stmt = conn.prepareStatement(
        	         "SELECT ticker, SUM(numStock) AS total_quantity, SUM(price*numStock) AS total_cost " +
        	         "FROM portfolio WHERE username = ? GROUP BY ticker")) {
        	    stmt.setString(1, username);
        	    try (ResultSet rs = stmt.executeQuery()) {
        	    	double totalMarketValue=0.0;
        	    	double accountValue=0.0;
        	        List<StockData> stocks = new ArrayList<>();
        	        while (rs.next()) {
        	            String symbol = rs.getString("ticker");
        	            int totalQuantity = rs.getInt("total_quantity");
        	            double totalCost = rs.getDouble("total_cost");
        	            double avgCostPerShare = totalQuantity > 0 ? totalCost / totalQuantity : 0;

        	            StockData stockData = fetchStockDataFromFinnhub(symbol);
        	            double change = stockData.getChange();
        	            double currentPrice = stockData.getCurrentPrice();
        	            double marketValue = totalQuantity * currentPrice;
        	            totalMarketValue+=marketValue;
        	            accountValue+=marketValue;
        	            stocks.add(new StockData(symbol, stockData.getCompanyName(), totalQuantity, avgCostPerShare, totalCost, change, currentPrice, marketValue));
        	        }
        	        
        	        double cashBalance = fetchCashBalance(username, conn);
        	      
        	        accountValue += cashBalance ;

        	        PortfolioResponse portfolioResponse = new PortfolioResponse(stocks, cashBalance, accountValue);
        	        Gson gson = new Gson();
        	        out.println(new Gson().toJson(portfolioResponse));
        	    }
        	} 
        }catch (SQLException | ClassNotFoundException ex) {
        	    ex.printStackTrace();
        	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	    response.getWriter().write("{\"error\":\"Database error occurred: " + ex.getMessage() + "\"}");
        	}
        out.close();
	}
    

    private double fetchCashBalance(String username, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT balance FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
            return 0; 
        }
    }

    @SuppressWarnings("deprecation")
	public StockData fetchStockDataFromFinnhub(String symbol) {
    	 final String API_KEY = "co0d7n9r01qg06er6730co0d7n9r01qg06er673g";
    	    final String QUOTE_URL = "https://finnhub.io/api/v1/quote?symbol=%s&token=%s";
    	    final String PROFILE_URL = "https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s";

    	    Gson gson = new Gson();
    	    Quote quote = null;
    	    Profile2 profile = null;

    	    // Fetch quote data
    	    try {
    	        URL url = new URL(String.format(QUOTE_URL, symbol, API_KEY));
    	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
    	        	
    	            quote = gson.fromJson(reader, Quote.class);
    	            System.out.println("inside PS API call: \n"+ quote.getC());
    	        }
    	    } catch (IOException e) {    	        
    	    	return null;
    	    }
    	    try {
    	        URL url2 = new URL(String.format(PROFILE_URL, symbol, API_KEY));
    	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()))) {
    	            profile = gson.fromJson(reader, Profile2.class);
    	        }
    	    } catch (IOException e) {
    	        return null;
    	    }

    	    if (quote == null || profile == null) {
    	        return null;
    	    }
    	    return new StockData(symbol, profile.getName(), 0,0.0, 0.0, quote.getD(), quote.getC(), 0.0);
    }
}
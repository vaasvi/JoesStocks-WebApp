package vaasvich_CSCI201_Assignment4;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


    @WebServlet("/TradeServlet")
    public class TradeServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          // Prepare to read the request body
          StringBuilder sb = new StringBuilder();
          String line;
          try (BufferedReader reader = request.getReader()) {
              while ((line = reader.readLine()) != null) {
              	//System.out.println(line);
                  sb.append(line);
              }
          } catch (Exception e) { 
              e.printStackTrace();
              response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
              return;
          }
          
          // Convert the JSON string to a Trade object using Gson
          String requestBody = sb.toString();
          Gson gson = new Gson();
          Trade trade = gson.fromJson(requestBody, Trade.class);
        	try {
        	    String resultMessage = insertTradeAndUpdateBalance(trade);
        	    if (resultMessage.startsWith("Bought")) {
        	        response.setStatus(HttpServletResponse.SC_OK);
        	        response.getWriter().write(resultMessage);
        	    } else {
        	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	        response.getWriter().write("FAILED: Purchase not possible.");
        	    }
        	} catch (Exception e) {
        	    e.printStackTrace();
        	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error occurred");
        	}
        }

        private boolean hasEnoughCash(String username, double totalCost) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/JoeStocks", "root", "Tusharchoudhary12!");
                 PreparedStatement ps = conn.prepareStatement("SELECT balance FROM users WHERE username = ?")) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    return currentBalance >= totalCost;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    
    private String insertTradeAndUpdateBalance(Trade trade) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement insertTradeStmt = null;
        PreparedStatement updateBalanceStmt = null;
        int tradeResult = -1;
        int balanceResult =-1;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/JoeStocks", "root", "Tusharchoudhary12!");
            conn.setAutoCommit(false);

            double totalCost = trade.getNumStock() * trade.getPrice();
            if(hasEnoughCash(trade.getUsername(), totalCost)) {
            insertTradeStmt = conn.prepareStatement("INSERT INTO portfolio (username, ticker, numStock, price) VALUES (?, ?, ?, ?)");
            insertTradeStmt.setString(1, trade.getUsername());
            insertTradeStmt.setString(2, trade.getTicker());
            insertTradeStmt.setInt(3, trade.getNumStock());
            insertTradeStmt.setDouble(4, trade.getPrice());
            tradeResult = insertTradeStmt.executeUpdate();

            updateBalanceStmt = conn.prepareStatement("UPDATE users SET balance = balance - ? WHERE username = ?");
            updateBalanceStmt.setDouble(1, totalCost);
            updateBalanceStmt.setString(2, trade.getUsername());
            balanceResult = updateBalanceStmt.executeUpdate();
            }

            if (tradeResult > 0 && balanceResult > 0) {
                conn.commit();
                return String.format("Bought %d shares of %s for $%.2f", trade.getNumStock(), trade.getTicker(), totalCost);
            } else {
                conn.rollback();
                return "FAILED: Purchase not possible.";
            }
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (insertTradeStmt != null) insertTradeStmt.close();
            if (updateBalanceStmt != null) updateBalanceStmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

}

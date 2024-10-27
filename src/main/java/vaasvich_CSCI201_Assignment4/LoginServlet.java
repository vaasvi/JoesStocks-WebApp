package vaasvich_CSCI201_Assignment4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); 

        // Database connection string
        String url = "jdbc:mysql://localhost/JoeStocks";
        String dbUsername = "root";
        String dbPassword = "Tusharchoudhary12!";
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
		             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
		            stmt.setString(1, username);

		            ResultSet rs = stmt.executeQuery();
		            if (rs.next()) {
		                String storedPassword = rs.getString("password");
		                if (password.equals(storedPassword)) {
		                    HttpSession session = request.getSession();

		                    session.setAttribute("username", username); 	                    
		                    response.getWriter().write("Login successful");
		                } else {
		                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		                    //CHATGPT Prompt: "response return for.." line 45
		                    response.getWriter().write("Invalid username or password");
		                }
		            } else {
		                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		                response.getWriter().write("Invalid username or password");
		            }
		        }
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }
}

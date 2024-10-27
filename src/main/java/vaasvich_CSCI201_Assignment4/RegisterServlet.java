package vaasvich_CSCI201_Assignment4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        double balance = 50000; // Default balance
        
        System.out.println("username: "+ username);
        System.out.println("password: "+password);
        System.out.println("email : "+ email);
        System.out.println();
        User newUser = new User(0, username, password, email, balance);

        // Database connection string
        String url = "jdbc:mysql://localhost/JoeStocks";
        String dbUsername = "root";
        String dbPassword = "Tusharchoudhary12!";
        
        Gson gson = new Gson();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, email, balance) VALUES (?, ?, ?, ?)")) {
                
                ps.setString(1, newUser.getUsername());
                ps.setString(2, newUser.getPassword());
                ps.setString(3, newUser.getEmail());
                ps.setDouble(4, newUser.getBalance());

                int result = ps.executeUpdate();
                if (result > 0) {
                    response.getWriter().println(gson.toJson(newUser));
                } else {
                    response.getWriter().println(gson.toJson("Registration failed"));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().print("Database error: " + e.getMessage());
        }
	}
}

package com.example.hotel;

import com.example.hotel.model.ConnectionPoolHolder;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.impl.JDBCUserDao;
import com.example.hotel.model.dao.mapper.UserMapper;
import com.example.hotel.model.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

//@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World! ALALLALAAL";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        ConnectionPoolHolder.getDataSource();
        Connection connection = ConnectionPoolHolder.getConnection();
        UserDao userDao = new JDBCUserDao(connection, new UserMapper());
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        try {
            out.println("<h1>" + userDao.findByFullName("anatoliy", "sirko0") + "</h1>");
        } catch (SQLException e) {
            out.println(e.getSQLState());
            throw new RuntimeException(e);
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPoolHolder.getDataSource();
        Connection connection = ConnectionPoolHolder.getConnection();
        UserDao userDao = new JDBCUserDao(connection, new UserMapper());
        try {
            User user = User.builder()
                    .email("tolik@gmail.com")
                    .firstname("anatoliy")
                    .lastname("sirko")
                    .login("anatoliy_123")
                    .phone("2131231212")
                    .password("qwerty54321")
                    .build();
            System.out.println(userDao.create(user));
            System.out.println(userDao.getCount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.doPost(req, resp);
    }

    public void destroy() {
    }
}
//package com.example.hotel;
//
//import com.example.hotel.controller.dto.UserDTO;
//import com.example.hotel.model.ConnectionPoolHolder;
//import com.example.hotel.model.dao.UserDao;
//import com.example.hotel.model.dao.impl.JDBCUserDao;
//import com.example.hotel.model.dao.mapper.RoleMapper;
//import com.example.hotel.model.dao.mapper.UserMapper;
//import com.example.hotel.model.entity.User;
//import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
//import com.example.hotel.model.service.exception.ServiceException;
//import com.example.hotel.model.service.impl.UserServiceImpl;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.SQLException;
//
////@WebServlet(name = "helloServlet", value = "/hello-servlet")
//public class HelloServlet extends HttpServlet {
//    private String message;
//
//    public void init() {
//        message = "Hello World! ALALLALAAL";
//    }
//
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("text/html");
//
//        // Hello
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        try {
//            var userService = new UserServiceImpl();
//            var userDTO = UserDTO.builder()
//                    .login("natasha_romanov")
//                    .email("natasha_romanov@gmail.com")
//                    .firstname("Natasha")
//                    .lastname("Romanov")
//                    .password("qwerty12345")
//                    .phone("+380938907381")
//                    .build();
////            var createdUser = userService.signUp(user);
////            var user = userService.signIn("aska", "2131231212");
//            var user = userService.signIn("Lupen_great", "qwerty54321");
//            out.println("<h1>" + user.get() + "</h1><br/><br/><br/>");
////            var apartment = Apartment
////                    .builder()
////                    .floor(1)
////                    .demand(5)
////                    .number(4)
////                    .price(BigDecimal.valueOf(32))
////                    .numberOfPeople(30)
////                    .apartmentStatus(BUSY)
////                    .apartmentClass(SUITE)
////                    .build();
////            apartmentDao.update(apartment);
////            var list = apartmentDao.findSortedByPriceDescending(0, 10);
////            for (var a : list) {
////                out.println(a);
////                out.println();
////            }
////            User user = User.builder().id(11L).build();
////            var application = Application
////                    .builder()
////                    .apartment(apartment)
////                    .client(user)
////                    .price(BigDecimal.valueOf(41231))
////                    .applicationStatus(CANCELED)
////                    .creationDate(LocalDateTime.of(2019, 1, 1, 1, 15))
////                    .lastModified(LocalDateTime.now())
////                    .startDate(LocalDateTime.now())
////                    .endDate(LocalDateTime.of(2023, 3, 3, 3, 12))
////                    .build();
////            applicationDao.create(application);
////            out.println("<h1>" + createdUser + "</h1><br/><br/><br/>");
//        } catch (LoginIsNotUniqueException e) {
//            e.printStackTrace(System.out);
//            throw new RuntimeException(e.getMessage());
//        } catch (ServiceException e){
//            System.out.println("Just plain service exception");
//            e.printStackTrace(System.out);
//            throw new RuntimeException(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//            throw new RuntimeException(e.getMessage());
//        }
//        out.println("</body></html>");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ConnectionPoolHolder.getDataSource();
//        Connection connection = ConnectionPoolHolder.getConnection();
//        UserDao userDao = new JDBCUserDao(connection, new UserMapper(), new RoleMapper());
//        try {
//            User user = User.builder()
//                    .email("tolik@gmail.com")
//                    .firstname("anatoliy")
//                    .lastname("sirko")
//                    .login("simple-login")
//                    .phone("2131231212")
//                    .password("qwerty54321")
//                    .build();
//            System.out.println(userDao.create(user));
//            System.out.println(userDao.getCount());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        super.doPost(req, resp);
//    }
//
//    public void destroy() {
//    }
//}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.ProductStatus;
import hibernate.User;
import hibernate.UserStatus;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "UserManagement", urlPatterns = {"/UserManagement"})
public class UserManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(User.class);
        List<User> userList = c1.list();

        Gson gson = new Gson();

        responseObject.add("userList", gson.toJsonTree(userList));
        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

    

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");

        JsonObject responseObject = new JsonObject();

        if (userId == null || userId.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "User ID is required.");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            s.beginTransaction();

            User user = (User) s.get(User.class, Integer.parseInt(userId));

            if (user != null) {
                s.delete(user);
                s.getTransaction().commit();
                s.close();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "User deleted successfully.");
            } else {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "User not found.");
            }
        }

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}

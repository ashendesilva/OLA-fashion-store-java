/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserStatus;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "UserStatusManagement", urlPatterns = {"/UserStatusManagement"})
public class UserStatusManagement extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String status = request.getParameter("status");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        User user = (User) s.get(User.class, Integer.valueOf(userId));
        UserStatus userStatus = (UserStatus) s.get(UserStatus.class, Integer.valueOf(status));

        if (user == null) {
            responseObject.addProperty("message", "Invalid User");
        } else {
            if (userStatus == null) {
                responseObject.addProperty("message", "Invalid Status");
            } else {
                user.setUserStatus(userStatus);
                s.saveOrUpdate(user);
                s.beginTransaction().commit();

                s.close();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "user status updated");

            }
        }
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }
}

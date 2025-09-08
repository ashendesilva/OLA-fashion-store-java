/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPassword extends HttpServlet {

    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        Gson gson = new Gson();
//        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);
//        String email = user.get("email").getAsString();
//
//        JsonObject responseObject = new JsonObject();
//        responseObject.addProperty("status", false);
//
//        if (email.isEmpty()) {
//            responseObject.addProperty("message", "Email can not be empty!");
//        } else if (!Util.isEmailValid(email)) {
//            responseObject.addProperty("message", "Please enter a valid email!");
//        } else {
//
//            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//            Session s = sessionFactory.openSession();
//
//            Criteria criteria = s.createCriteria(User.class);
//
//            criteria.add(Restrictions.eq("email", email));
//
//            List<User> users = criteria.list();
//            if (users.isEmpty()) {
//                responseObject.addProperty("message", "No user found. Invalid Email");
//            } else {
//                User existingUser = users.get(0); // Get the existing user
//                final String resetCode = Util.generateCode();
//                existingUser.setVerification(resetCode);
//                s.update(existingUser); // Update instead of save
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Mail.sendMail(email,
//                                "OLA Fashion Store - Password Reset",
//                                "<!DOCTYPE html>"
//                                + "<html>"
//                                + "<head><meta charset='UTF-8'></head>"
//                                + "<body style='font-family: Arial, sans-serif;'>"
//                                + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc;'>"
//                                + "<h2 style='color: #2c3e50;'>OLA Fashion Store - Password Reset</h2>"
//                                + "<p>We received a request to reset your <strong>OLA Fashion Store</strong> account password.</p>"
//                                + "<p>Please use the reset code below to proceed:</p>"
//                                + "<div style='font-size: 24px; font-weight: bold; margin: 20px 0; color: #e67e22;'>"
//                                + resetCode
//                                + "</div>"
//                                + "<p>This code will expire in 10 minutes for your security.</p>"
//                                + "<p>If you did not request a password reset, please ignore this email.</p>"
//                                + "<p>Best regards,<br/>OLA Fashion Store Team</p>"
//                                + "</div>"
//                                + "</body>"
//                                + "</html>"
//                        );
//                    }
//                }).start();
//
//                // Create session
//                HttpSession ses = request.getSession();
//                ses.setAttribute("email", email);
//
//                responseObject.addProperty("status", true);
//                responseObject.addProperty("message", "Reset code send successfully");
//
//            }
//            s.close();
//        }
//        String responseText = gson.toJson(responseObject);
//        response.setContentType("application/json");
//        response.getWriter().write(responseText);
//
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);
        String email = user.get("email").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (email.isEmpty()) {
            responseObject.addProperty("message", "Email can not be empty!");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Please enter a valid email!");
        } else {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session s = sessionFactory.openSession();
            Transaction tx = null;

            try {
                tx = s.beginTransaction(); // Start transaction

                Criteria criteria = s.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", email));

                List<User> users = criteria.list();
                if (users.isEmpty()) {
                    responseObject.addProperty("message", "No user found. Invalid Email");
                } else {
                    User existingUser = users.get(0);
                    final String resetCode = Util.generateCode();
                    existingUser.setVerification(resetCode);

                    s.update(existingUser); // Update the existing user
                    s.flush(); // Force the update to execute immediately

                    // Start email thread after successful DB update
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Mail.sendMail(email,
                                    "OLA Fashion Store - Password Reset",
                                    "<!DOCTYPE html>"
                                    + "<html>"
                                    + "<head><meta charset='UTF-8'></head>"
                                    + "<body style='font-family: Arial, sans-serif;'>"
                                    + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc;'>"
                                    + "<h2 style='color: #2c3e50;'>OLA Fashion Store - Password Reset</h2>"
                                    + "<p>We received a request to reset your <strong>OLA Fashion Store</strong> account password.</p>"
                                    + "<p>Please use the reset code below to proceed:</p>"
                                    + "<div style='font-size: 24px; font-weight: bold; margin: 20px 0; color: #e67e22;'>"
                                    + resetCode
                                    + "</div>"
                                    + "<p>This code will expire in 10 minutes for your security.</p>"
                                    + "<p>If you did not request a password reset, please ignore this email.</p>"
                                    + "<p>Best regards,<br/>OLA Fashion Store Team</p>"
                                    + "</div>"
                                    + "</body>"
                                    + "</html>"
                            );
                        }
                    }).start();

                    HttpSession ses = request.getSession();
                    ses.setAttribute("email", email);

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Reset code sent successfully");
                }

                tx.commit(); // Commit the transaction
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback(); // Rollback if there's an error
                }
                responseObject.addProperty("message", "Error processing your request");
                e.printStackTrace();
            } finally {
                s.close(); // Always close the session
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}

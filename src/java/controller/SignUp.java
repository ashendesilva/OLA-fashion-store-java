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
import hibernate.UserType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject resJsonObject = new JsonObject();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String firstname = user.get("firstname").getAsString();
        String lastname = user.get("lastname").getAsString();
        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();
        String mobile = user.get("mobile").getAsString();

       
        // Validation
        if (firstname.isEmpty()) {
            resJsonObject.addProperty("message", "First Name is required");
        } else if (lastname.isEmpty()) {
            resJsonObject.addProperty("message", "Last Name is required");
        } else if (email.isEmpty()) {
            resJsonObject.addProperty("message", "Email is required");
        } else if (!Util.isEmailValid(email)) {
            resJsonObject.addProperty("message", "Please enter a valid email");
        } else if (mobile.isEmpty()) {
            resJsonObject.addProperty("message", "Mobile is required");
        } else if (!mobile.matches("^(07[0-9]{8})$")) {
            resJsonObject.addProperty("message", "Invalid Sri Lanka mobile number format! ex:0773334441");
        } else if (password.isEmpty()) {
            resJsonObject.addProperty("message", "Password is required");
        } else if (!Util.isPasswordValid(password)) {
            resJsonObject.addProperty("message", "The password must contains at least uppercase, lowecase,"
                    + " number, special character and to be minimum eight characters long!");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            // Check if user exists
            Criteria criteria = s.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));

            if (!criteria.list().isEmpty()) {
                resJsonObject.addProperty("message", "User with email already exists");
            } else {

                String usertypeId = ("1");
                UserType userType = (UserType) s.load(UserType.class, Integer.parseInt(usertypeId));
                UserStatus userStatus = (UserStatus) s.load(UserStatus.class, 1);

                User u = new User();
                u.setF_name(firstname);
                u.setL_name(lastname);
                u.setEmail(email);
                u.setPassword(password);
                u.setPhone(mobile);
                u.setCreated_at(new Date());
                String verificationCode = Util.generateCode();
                u.setVerification(verificationCode);
                u.setUserType(userType);
                u.setUserStatus(userStatus);

                s.save(u);
                s.beginTransaction().commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "OLA Fashion Store - Verification", "<!DOCTYPE html>"
                                + "<html>"
                                + "<head><meta charset='UTF-8'></head>"
                                + "<body style='font-family: Arial, sans-serif;'>"
                                + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ccc;'>"
                                + "<h2 style='color: #2c3e50;'>OLA Fashion Store - Email Verification</h2>"
                                + "<p>Hello," + firstname + "</p>"
                                + "<p>Thank you for registering with <strong>OLA Fashion Store</strong>.</p>"
                                + "<p>Please use the verification code below to complete your registration:</p>"
                                + "<div style='font-size: 24px; font-weight: bold; margin: 20px 0; color: #27ae60;'>"
                                + verificationCode
                                + "</div>"
                                + "<p>If you did not request this, please ignore this email.</p>"
                                + "<p>Best regards,<br/>OLA Fashion Store Team</p>"
                                + "</div>"
                                + "</body>"
                                + "</html>");
                    }
                }).start();

                // Create session
                HttpSession ses = request.getSession();
                ses.setAttribute("email", email);

                resJsonObject.addProperty("status", true);
                resJsonObject.addProperty("message", "Register success. Please check your email for verification code");
            }
            s.close();
        }

        String responseText = gson.toJson(resJsonObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}

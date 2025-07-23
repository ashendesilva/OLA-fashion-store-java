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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject signIn = gson.fromJson(request.getReader(), JsonObject.class);

        String email = signIn.get("email").getAsString();
        String password = signIn.get("password").getAsString();

        JsonObject resJsonObject = new JsonObject();
        resJsonObject.addProperty("status", false);

        if (email.isEmpty()) {
            resJsonObject.addProperty("message", "Email is required");
        } else if (!Util.isEmailValid(email)) {
            resJsonObject.addProperty("message", "Please enter a valid email");
        } else if (password.isEmpty()) {
            resJsonObject.addProperty("message", "Password is required");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(User.class);

            Criterion crt1 = Restrictions.eq("email", email);
            Criterion crt2 = Restrictions.eq("password", password);

            c.add(crt1);
            c.add(crt2);

            if (c.list().isEmpty()) {
                resJsonObject.addProperty("message", "Invalid credentials");
            } else {
                User u = (User) c.list().get(0);
                resJsonObject.addProperty("status", true);
                HttpSession ses = request.getSession();
                
                if (u.getUserStatus().getId() == 2) {
                    resJsonObject.addProperty("status", false);
                    resJsonObject.addProperty("message", "Inactive User.");
                } else {
                    resJsonObject.addProperty("status", true);
                    if (u.getVerification().equals("Verified")) {
                        ses.setAttribute("user", u);
                        resJsonObject.addProperty("message", "2");
                    } else {
                        ses.setAttribute("email", email);
                        resJsonObject.addProperty("message", "1");
                    }

                }
            }
            s.close();

        }
        String responseText = gson.toJson(resJsonObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}

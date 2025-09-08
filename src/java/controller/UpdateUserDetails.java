/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
@WebServlet(name = "UpdateUserDetails", urlPatterns = {"/UpdateUserDetails"})
public class UpdateUserDetails extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {

        JsonObject resJsonObject = new JsonObject();
        resJsonObject.addProperty("status", false);
        Gson gson = new Gson();

        JsonObject userData = gson.fromJson(reqest.getReader(), JsonObject.class);

        String phone = userData.get("phone").getAsString();
        String lastName = userData.get("lastName").getAsString();
        String firstName = userData.get("firstName").getAsString();
        String email = userData.get("email").getAsString();

        System.out.println(phone);
        System.out.println(lastName);
        System.out.println(firstName);
        System.out.println(email);

        if (email.isEmpty()) {
            resJsonObject.addProperty("message", "Invalid User");
        } else if (firstName.isEmpty()) {
            resJsonObject.addProperty("message", "first name can not be empty!");
        } else if (lastName.isEmpty()) {
            resJsonObject.addProperty("message", "last name can not be empty!");
        } else if (phone.isEmpty()) {
            resJsonObject.addProperty("message", "mobile number can not be empty!");
        } else if (!phone.matches("^(07[0-9]{8})$")) {
            resJsonObject.addProperty("message", "Invalid Sri Lanka mobile number format!");
        } else {
                SessionFactory sf = hibernate.HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                Criteria c1 = s.createCriteria(User.class);
                Criterion crt1 = Restrictions.eq("email", email);

                c1.add(crt1);

                if (c1.list().isEmpty()) {
                    resJsonObject.addProperty("message", "Invalid User");
                } else {
                    User user = (User) c1.list().get(0);
                    user.setF_name(firstName);
                    user.setL_name(lastName);
                    user.setPhone(phone);

                    s.update(user);
                    s.beginTransaction().commit();
                    s.close();
                    
                    HttpSession ses = reqest.getSession();

                    ses.setAttribute("user", user);

                    resJsonObject.addProperty("status", true);
                    resJsonObject.addProperty("message", "User Update succesful");
                }
            }
            String responseTesxt = gson.toJson(resJsonObject);
            response.setContentType("application/json");
            response.getWriter().write(responseTesxt);
        }

    }

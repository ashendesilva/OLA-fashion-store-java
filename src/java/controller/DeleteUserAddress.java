/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.Orders;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "DeleteUserAddress", urlPatterns = {"/DeleteUserAddress"})
public class DeleteUserAddress extends HttpServlet {

    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String addressId = request.getParameter("addressID");
//
//        JsonObject responseObject = new JsonObject();
//
//        if (addressId == null || addressId.isEmpty()) {
//            responseObject.addProperty("status", false);
//            responseObject.addProperty("message", "Address ID is required.");
//        } else {
//            SessionFactory sf = HibernateUtil.getSessionFactory();
//            Session s = sf.openSession();
//            s.beginTransaction();
//
//            Address address = (Address) s.get(Address.class, Integer.parseInt(addressId));
////            User user = (User) s.get(User.class, Integer.parseInt(addressId));
//
//            if (address != null) {
//                s.delete(address);
//                s.getTransaction().commit();
//                s.close();
//                responseObject.addProperty("status", true);
//                responseObject.addProperty("message", "Address deleted successfully.");
//            } else {
//                responseObject.addProperty("status", false);
//                responseObject.addProperty("message", "Addsess not found.");
//            }
//        }
//
//        Gson gson = new Gson();
//        response.setContentType("application/json");
//        response.getWriter().write(gson.toJson(responseObject));
//    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String addressId = request.getParameter("addressID");

        JsonObject responseObject = new JsonObject();

        if (addressId == null || addressId.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Address ID is required.");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            s.beginTransaction();

            Address address = (Address) s.get(Address.class, Integer.parseInt(addressId));

            if (address != null) {
                Criteria criteria = s.createCriteria(Orders.class);
                criteria.add(Restrictions.eq("address", address));

                if (!criteria.list().isEmpty()) {
                    responseObject.addProperty("message", "This address canot be delete.");
                } else {
                    s.delete(address);
                    s.getTransaction().commit();
                    s.close();
                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Address deleted successfully.");
                }

            } else {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Addsess not found.");
            }
            }

            Gson gson = new Gson();
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseObject));
        }

    }

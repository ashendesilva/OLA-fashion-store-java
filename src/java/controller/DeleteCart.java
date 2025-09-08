/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.Orders;
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
@WebServlet(name = "DeleteCart", urlPatterns = {"/DeleteCart"})
public class DeleteCart extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cartId = request.getParameter("cartId");

        JsonObject responseObject = new JsonObject();

        if (cartId == null || cartId.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Cart ID is required.");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            s.beginTransaction();

            Cart cart = (Cart) s.get(Cart.class, Integer.parseInt(cartId));

            if (cart != null) {

                s.delete(cart);
                s.getTransaction().commit();
                s.close();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "cart deleted successfully.");

            } else {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Cart not found.");
            }
        }

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}



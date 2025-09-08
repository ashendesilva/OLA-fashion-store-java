/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "UpdateCartItemQty", urlPatterns = {"/UpdateCartItemQty"})
public class UpdateCartItemQty extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject cartDetails = gson.fromJson(request.getReader(), JsonObject.class);

//        String cartId = request.getParameter("cartItemId");
//        String newQty = request.getParameter("newQty");
        String newQty = cartDetails.get("newQty").getAsString();
        String cartId = cartDetails.get("cartItemId").getAsString();

        System.out.println(cartId + newQty);

        JsonObject responseObject = new JsonObject();
        response.setContentType("application/json");

        // Validate inputs
        if (cartId == null || cartId.isEmpty() || newQty == null || newQty.isEmpty()) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Cart ID and quantity are required.");
            response.getWriter().write(gson.toJson(responseObject));
            return;
        }

        try {
            int quantity = Integer.parseInt(newQty);
            if (quantity < 1) {
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Quantity must be at least 1.");
                response.getWriter().write(gson.toJson(responseObject));
                return;
            }

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session session = sf.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                Cart cart = (Cart) session.get(Cart.class, Integer.parseInt(cartId));

                if (cart != null) {
                    cart.setQty(quantity);
                    session.update(cart);
                    transaction.commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Quantity updated successfully.");
                } else {
                    responseObject.addProperty("status", false);
                    responseObject.addProperty("message", "Cart item not found.");
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                responseObject.addProperty("status", false);
                responseObject.addProperty("message", "Error updating quantity: " + e.getMessage());
            } finally {
                session.close();
            }
        } catch (NumberFormatException e) {
            responseObject.addProperty("status", false);
            responseObject.addProperty("message", "Invalid quantity format.");
        }

        response.getWriter().write(gson.toJson(responseObject));
    }
}

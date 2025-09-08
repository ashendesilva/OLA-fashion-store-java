/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.HibernateUtil;
import hibernate.OrderItems;
import hibernate.Orders;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "LoadUserOrders", urlPatterns = {"/LoadUserOrders"})
public class LoadUserOrders extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null && ses.getAttribute("user") != null) {
            Gson gson = new Gson();
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            JsonObject responseObject = new JsonObject();
            User user = (User) ses.getAttribute("user");

            try {
                Criteria orderCriteria = s.createCriteria(Orders.class);
                orderCriteria.add(Restrictions.eq("user", user));
                List<Orders> orderList = orderCriteria.list();

                if (!orderList.isEmpty()) {
                    JsonArray ordersArray = new JsonArray();
                    for (Orders order : orderList) {
                        Criteria itemCriteria = s.createCriteria(OrderItems.class);
                        itemCriteria.add(Restrictions.eq("orders", order));
                        List<OrderItems> itemList = itemCriteria.list();

                        int totalItems = 0;
                        double totalPrice = 0.0;

                        for (OrderItems item : itemList) {
                            totalItems += item.getQty();
                            totalPrice += item.getQty() * item.getProduct().getPrice();
                        }

                        JsonObject summary = new JsonObject();
                        summary.addProperty("orderId", order.getId());
                        summary.addProperty("date", order.getCreatedAt().toString()); 
                        summary.addProperty("totalItems", totalItems);
                        summary.addProperty("totalPrice", totalPrice);

                        ordersArray.add(summary);
                    }

                    responseObject.add("orders", ordersArray);
                }

                String toJson = gson.toJson(responseObject);
                response.setContentType("application/json");
                response.getWriter().write(toJson);
            } finally {
                s.close();
            }
        }

    }

}

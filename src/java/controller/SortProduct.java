/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.Product;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "SortProduct", urlPatterns = {"/SortProduct"})
public class SortProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        
        Criteria c1 = s.createCriteria(Product.class);
        c1.add(Restrictions.eq("productStatus.id", 1));
        
        if (requestJsonObject.has("sortValue")) {
            String sortValue = requestJsonObject.get("sortValue").getAsString();
            if (sortValue.equals("Sort by Latest")) {
                c1.addOrder(Order.desc("id"));
            } else if (sortValue.equals("Sort by Oldest")) {
                c1.addOrder(Order.asc("id"));
            } else if (sortValue.equals("Sort by Name")) {
                c1.addOrder(Order.asc("title"));
            } else if (sortValue.equals("Sort by Price")) {
                c1.addOrder(Order.asc("price"));
            }
        }
        
        List<Product> productList = c1.list();

        // Close session
        s.close();

        // Return response
        resposeObject.add("productList", gson.toJsonTree(productList));
        resposeObject.addProperty("status", true);
        String toJson = gson.toJson(resposeObject);
        response.getWriter().write(toJson);
        
    }

}

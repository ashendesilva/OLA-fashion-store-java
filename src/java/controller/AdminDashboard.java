/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.Category;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Orders;
import hibernate.Product;
import hibernate.ProductStatus;
import hibernate.Size;
import hibernate.SubCategory;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
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
@WebServlet(name = "AdminDashboard", urlPatterns = {"/AdminDashboard"})
public class AdminDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //get-quality
        Criteria c6 = s.createCriteria(Product.class);
        List<Product> productList = c6.list();
        int totalProducts = productList.size();
        //get-quality-end

        Criteria c1 = s.createCriteria(User.class);
        List<User> userList = c1.list();
        int totalu = userList.size();

        Criteria c2 = s.createCriteria(Orders.class);
        List<Orders> OrdersList = c2.list();
        int totalOrders = OrdersList.size();

        Gson gson = new Gson();

        responseObject.add("totalProduct", gson.toJsonTree(totalProducts));
        responseObject.add("totalUser", gson.toJsonTree(totalu));
        responseObject.add("totalOrders", gson.toJsonTree(totalOrders));

        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject category = gson.fromJson(request.getReader(), JsonObject.class);
        String categoryName = category.get("categoryName").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = null;

        try {
            //validation
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                responseObject.addProperty("message", "Please sign in!");
            } else if (user.getUserType().getId() != 2) {
                responseObject.addProperty("message", "You do not have permission to add products.");
            } else if (categoryName.isEmpty()) {
                responseObject.addProperty("message", "Category Name can not be empty");
            } else {
                tx = s.beginTransaction();

                Category c = new Category();
                c.setName(categoryName);

                s.save(c);

                tx.commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Category registered successfully");
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            responseObject.addProperty("message", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            s.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}

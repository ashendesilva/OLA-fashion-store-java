/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
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
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        String productId = request.getParameter("id");
        if (Util.isInteger(productId)) {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            try {
                Product product = (Product) s.get(Product.class, Integer.valueOf(productId));
                if (product.getProductStatus().getName().equals("Available")) {

//                    // similer-product-data
                    Criteria c1 = s.createCriteria(Category.class);
                    c1.add(Restrictions.eq("name", product.getCategory().getName()));
                    List<Category> categorytList = c1.list();
                    if (!categorytList.isEmpty()) {
                        Criteria c2 = s.createCriteria(Product.class);
                        c2.add(Restrictions.in("category", categorytList));
                        c2.add(Restrictions.ne("id", product.getId()));
                        c2.add(Restrictions.eq("productStatus.id", 1));
                        c2.setMaxResults(4);
                        List<Product> productList = c2.list();

                        responseObject.add("product", gson.toJsonTree(product));
                        responseObject.add("productList", gson.toJsonTree(productList));
                        responseObject.addProperty("status", true);
                    } else {
                        responseObject.addProperty("message", "Category not found.");
                    }
                    // similer-product-data-end

                } else {
                    responseObject.addProperty("message", "Product Not Found!");
                }
            } catch (Exception e) {
                responseObject.addProperty("message", "Product Not Found!");
            }
        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);
    }

}
